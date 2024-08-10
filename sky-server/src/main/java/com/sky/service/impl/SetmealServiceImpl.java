package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 套餐业务接口实现类
 * @create: 2024/8/4 10:04
 **/
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 10:28
     * @desc: 插入套餐
     * @return: void
     */
    @Override
    @Transactional
    public void insertWithDish(SetmealDTO setmealDTO) {
        //创建新对象并复制属性
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //setmeal.setStatus(StatusConstant.DISABLE);//新增的套餐默认处于禁用状态，源码里没有这一行，因为源码的前端已经默认设置为DISABLE，不需要后端业务再继续处理
        setmealMapper.insert(setmeal);//插入赋值好的对象

        //准备插入套餐菜品关联表
        Long setmealId = setmealDTO.getId();

        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if (setmealDishList != null && setmealDishList.size() != 0) {//确保非空且长度为0，防止前端页面没传数据过来，导致出错
            //遍历刚才从DTO对象里取得的关联列表
            setmealDishList.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);//类似于之前写的新建菜品功能，DTO对象里都多出一个列表属性，原bean里没有
            });
            setmealDishMapper.insertBatch(setmealDishList);//只有关联表有值才过来赋值
        }
    }

    /**
     * @param setmealPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/8/4 12:33
     * @desc: 分页查询
     * @return: com.sky.result.PageResult
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());//这里第二个域写成了getPage()，导致查询出现问题
        Page<Setmeal> page = (Page<Setmeal>) setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/8/5 9:02
     * @desc: 套餐批量删除
     * @return: void
     */
    @Override
    @Transactional
    public void deleteByIDs(List<Long> ids) {
        //我们假设理想情况下，ids列表不可能为空
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            //未启售中的套餐才能删除，这一部分我和源码不同，我认为我的版本也可
            if (setmeal.getStatus().equals(StatusConstant.DISABLE)) {
                setmealMapper.deleteById(id); //这里我顺序还搞反了，正常来说要先删除套餐，再删除关联。
                setmealDishMapper.deleteBatchBySetmealId(id);
            } else {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);//启售中的套餐不能删除
            }
        });

        //源码是先遍历，只要有一个处于停售，就直接抛出错误，最后重新遍历一遍，给套餐表和关联表赋值。我是在过程中用if分支控制，也能起到同样效果，但是效率低

        /*ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if(StatusConstant.ENABLE == setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(setmealId);
        });*/
    }

    /**
     * @param setmealDTO
     * @author: xuwuyuan
     * @date: 2024/8/5 9:46
     * @desc: 根据DTO对象来修改套餐，并且更改关联表，确保菜品变动都会实时反映在关联表里
     * @return: void
     */
    @Override
    @Transactional
    public void updateWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBatchBySetmealId(setmealId);//一旦setmeal_id域等于DTO对象里的setmealId属性，立刻批量删除

        //取得DTO对象里的setmealDishes列表属性，然后在每个列表元素里取得setmealId属性，这就是要准备插入到里面的
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if (setmealDishList != null && setmealDishList.size() != 0) {
            setmealDishList.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);//插入的这些关联表属性就差了套餐id属性，所以现在全部填进去
            });
            setmealDishMapper.insertBatch(setmealDishList);//这句话漏了，所以最后无法生效。无法全部插入
        }
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/5 10:10
     * @desc: 根据id查找套餐
     * @return: com.sky.entity.Setmeal
     */
    @Override
    public SetmealVO findByIdWithDish(Long id) {
        //在SetmealVO中先搞定除了setmealdishes属性以外的属性
        Setmeal setmeal = setmealMapper.getById(id);//查询到的套餐对象
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        //然后查找关联表，得到setmealVO的SetmealDishes属性
        List<SetmealDish> dishes = setmealDishMapper.findBySetmealId(id);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/6 8:48
     * @desc: 套餐启停
     * @return: void
     */
    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        //针对启用套餐请求的单独处理，防止启用的套餐中包含处于禁用状态的菜品
        if (status.equals(StatusConstant.ENABLE)) {
            //dish表左连接setmeal_dish，因为有的dish可能没有和setmeal关联，这个也要算进去。
            //底层实现原理是select d.* from dish d left join setmeal_dish sd on d.id = sd.dish_id
            List<Dish> dishes = dishMapper.getDishesBySetmealId(id);
            for (Dish dish : dishes) {
                if (dish.getStatus().equals(StatusConstant.DISABLE))
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);//套餐包含未启售菜品，所以直接抛错
            }
        }
        //以下为启用和禁用两个请求都需要执行的代码段，只不过如果启用请求不符合业务逻辑，在上一个if分支就被throw掉了，不会走到这里
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/9 22:29
     * @desc: 根据分类id列出套餐
     * @param categoryId
     * @return: java.util.List<com.sky.entity.Setmeal>
     */
    @Override
    public List<Setmeal> listByCategoryId(Long categoryId) {
        Setmeal setmeal = Setmeal.builder()
                .status(StatusConstant.ENABLE)
                .categoryId(categoryId)
                .build();
        return setmealMapper.list(setmeal);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/10 9:44
     * @desc: 通过套餐id查询菜品视图输出列表
     * @param setmealId
     * @return: java.util.List<com.sky.vo.DishItemVO>
     */
    @Override
    public List<DishItemVO> listDishItemBySetmealId(Long setmealId) {
        return setmealMapper.getDishItemsBySetmealId(setmealId);
    }
}
