package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.controller.admin.DishController;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.*;
import com.sky.exception.*;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品业务实现类
 * @create: 2024/7/29 17:53
 **/
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/7/29 20:41
     * @desc: 新增菜品实现类
     * @return: void
     */
    @Override
    @Transactional //该方法先后涉及dishMapper和dishFlavorMapper的操作.所以要声明事务，确保失败回滚
    public void save(DishDTO dishDTO) {
        //先对DTO对象的categoryId属性做一个限制，如果为空，直接报错。这个是源码里没有的内容，但实际上前端代码里对分类id属性做了限制，不用我这里多此一举。
        if (dishDTO.getCategoryId() == null) throw new FieldMissingException("操你妈！categoryId属性为什么不给？");

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();//获取mapper的回显id属性，用于后续插入dish_flavor表

        //获取dishflavors列表以后，使用foreach方法和lambda表达式来遍历
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() != 0) {//这里我漏了判断条件，如果输入DTO对象没有dishFlavors列表，那就不用插入了
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
        }
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * @param dishPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/30 10:18
     * @desc: 菜品分页查询
     * @return: com.sky.result.PageResult
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/7/31 8:38
     * @desc: 菜品批量删除
     * @return: void
     */
    @Override
    @Transactional //该方法先后涉及dishMapper和dishFlavorMapper的操作.所以要声明事务，确保失败回滚
    public void deleteBatch(List<Long> ids) {
        //遍历ids列表可以使用Stream类的foreach方法加lambda表达式，或者foreach循环
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);

            //增加错误控制，使得系统抛出异常，不会直接报错。有时使用id直接查询dish表，会出现不存在该id的记录。这时就要抛出异常。
            if (dish == null || dish.getStatus() == null)
                throw new NoSuchRecordException(MessageConstant.RECORD_NOT_FOUND);

            //先检查是否有菜品处于启售状态中，启售菜品不能删除。
            if (dish.getStatus().equals(1)) throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);

            //检查是否有菜品和套餐关联，一旦有关联就不能删除。这个部分和源码不同，我的方案不好。没有在循环外通过ids列表查询套餐菜品关联表，这样做效率会降低，因为多次调用dao层sql语句
            /*List<Long> setmealIDs= setmealDishMapper.findSetmealIDsByDishId(id);
            if (setmealIDs != null && setmealIDs.size() != 0) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }*/
        }
        //检查是否有菜品和套餐关联，一旦有关联就不能删除。以下为源码实现
        List<Long> setmealIDs = setmealDishMapper.getSetmealIDsByDishIDs(ids);
        if (setmealIDs != null && setmealIDs.size() != 0)
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        //进行菜品删除操作，先删除dish表里的记录。我认为可以不在这一层里遍历，转而在dao层里面直接编写动态sql批量删除语句。这样可以少几次sql查询语句
        dishMapper.deleteBatch(ids);

        //菜品口味删除在后面实现，也只需要执行一次，不用遍历ids里的每一个id。反正有事务回滚注解，出了问题会回退。
        dishFlavorMapper.deleteBatchByDishIDs(ids);//如果口味表里没有一条记录的dish_id字段对应当前的ids属性，那就算输入指令，mysql也不会实际操作。

        //我的方式和源码的方式各有好处，他那么做事务回滚更保险，只要一个dishid操作失败，数据都能恢复到最初。而我这么做，只要有一个id操作失败，而且数据库里也没加事务回滚，那就出现事务不一致了。
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/31 12:25
     * @desc: 获取dishvo对象。相比于dish对象，dishvo增加了属性flavors列表
     * @return: com.sky.vo.DishVO
     */
    @Override
    //这里不需要事务注解，因为不会动数据库
    public DishVO getByIdWithFlavor(Long id) {
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        //将dish里的对象全部输入给VO视图对象，然后再设置一个口味。
        Dish dish = dishMapper.getById(id); //感兴趣的话，这里还能加一个抛出错误，避免dish为0
        DishVO dishVO = new DishVO();
        if (dish == null) throw new NoSuchRecordException(MessageConstant.RECORD_NOT_FOUND);//错误控制写在这里，也可以加一些其他语句
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/8/3 15:25
     * @desc: 修改菜品
     * @return: void
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //批量删除口味，准备之后再重新插入
        Long dishId = dishDTO.getId();//取得输入对象的id，即为dishid
        List<Long> dishIDs = new ArrayList();
        dishIDs.add(dishId);//将唯一的dishId插入列表容器
        dishFlavorMapper.deleteBatchByDishIDs(dishIDs);

        //批量插入变更后的口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //遍历列表里的所有口味，并适当添加。
        if (flavors != null && flavors.size() != 0)
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/3 21:03
     * @desc: 菜品启售停售功能
     * @return: void
     */
    @Override
    @Transactional //源码用了事务注解声明，我自己的实现没使用。因为我自己的实现不包括对两个dao层的修改请求
    public void startOrStop(Integer status, Long id) {
        //以下是我的代码，和源码相比，我认为受套餐限制时无法禁用某个菜品，源码认为禁用一个菜品只要确保同时禁用潜在关联的套餐即可，属于业务模糊
        /*List<Long> idList = new ArrayList<>();
        idList.add(id);//构造一个List，只含有一个元素
        List<Long> setmealIDs = setmealDishMapper.getSetmealIDsByDishIDs(idList);//如果有个方法参数是Long类型的id，那就不用生造一个List然后插入单个元素了

        //需要防止因为禁用该菜品，导致某个发售中的套餐失效。所以提前加一个预处理，截断违规操作(停用菜品的同时导致发售中的某个套餐失去了一个菜品)
        for (Long setmealID : setmealIDs) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == 1)
                throw new DishDisableFailedException(MessageConstant.DISH_DISABLE_FAILED);//这里应该抛出DishEnableFailed异常，这是我自己定义的异常类和消息常量
        }

        //对于启用和禁用，以下代码都适用。调用dao层请求更新数据库。
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);*/

        //以下为源码
        //创建dish对象并且借助它更新数据层
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        //获取关联该菜品id的所有套餐对象
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        List<Long> setmealIDs = setmealDishMapper.getSetmealIDsByDishIDs(idList);

        //先判断获取到的套餐列表是否为空。获得空列表，说明该菜品没有关联套餐。这里我漏了这个判断条件，一旦套餐列表为null，后面一行马上会报错，nullPointerException
        if (setmealIDs != null && setmealIDs.size() != 0) {
            //将这些套餐对象全部禁用，这里优先使用增强for循环，因为Stream类的foreach方法和lambda表达式只适合短句
            for (Long setmealId : setmealIDs) {
                if (status == StatusConstant.DISABLE) {//如果输入请求为禁用，那么就要顺便禁止关联的在售套餐
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId) //规定查询对象的id等于刚才查到的套餐id
                            .status(status)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * @param categoryId
     * @author: xuwuyuan
     * @date: 2024/8/5 11:24
     * @desc: 返回菜品列表，用于套餐菜品选择功能
     * @return: java.util.List<com.sky.entity.Dish>
     */
    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .status(StatusConstant.ENABLE) //确保只返回已启用的菜品
                .categoryId(categoryId)
                .build();
        return dishMapper.list(dish);
    }


    /**
     * @param dishQuery
     * @author: xuwuyuan
     * @date: 2024/8/8 10:58
     * @desc: 返回带口味的列表
     * @return: java.leih
     */
    @Override
    public List<DishVO> listByCategoryIdWithFlavor(Dish dishQuery) {
        //输入的user带有categoryId属性，并且确保已经启用
        List<DishVO> resList = new ArrayList<>();//该列表用于存放DishVO实体类，之后输出
        List<Dish> dishList = dishMapper.list(dishQuery);

        //遍历所有dishList中的对象
        for (Dish dish : dishList) {
            Long dishId = dish.getId();
            List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(dishId);
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);//拷贝基础部分

            //还差了一个属性，dishFlavor列表
            dishVO.setFlavors(dishFlavors);
            resList.add(dishVO);
        }
        return resList;
    }
}
