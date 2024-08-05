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
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        }
        setmealDishMapper.insertBatch(setmealDishList);
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
            //未启售中的套餐才能删除
            if (setmeal.getStatus().equals(StatusConstant.DISABLE)) {
                setmealDishMapper.deleteBatchBySetmealId(id);
                setmealMapper.deleteById(id);
            } else {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);//启售中的套餐不能删除
            }
        });
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
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBatchBySetmealId(setmealId);//一旦setmeal_id域等于DTO对象里的setmealId属性，立刻批量删除

        //取得DTO对象里的setmealDishes列表属性，然后在每个列表元素里取得setmealId属性，这就是要准备插入到里面的
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if (setmealDishList != null && setmealDishList.size() != 0) {
            setmealDishList.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);//插入的这些关联表属性就差了套餐id属性，所以现在全部设置进去
            });
        }
        setmealDishMapper.insertBatch(setmealDishList);//这句话漏了，所以最后无法生效。无法全部插入
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
        Setmeal setmeal = setmealMapper.getById(id);//查询到的套餐对象
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        //然后查找关联表，得到setmealVO的SetmealDishes属性
        List<SetmealDish> dishes = setmealDishMapper.findBySetmealId(id);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }
}
