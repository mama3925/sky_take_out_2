package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.controller.admin.DishController;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.FieldMissingException;
import com.sky.exception.NoSuchRecordException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
    private DishController dishController;

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
    @Transactional //涉及两个持久层的操纵，分别是dishMapper和dishFlavorMapper
    public DishVO getByIdWithFlavor(Long id) {
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);//获取当前dishId的口味列表

        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }
}
