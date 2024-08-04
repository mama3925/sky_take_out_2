package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @author: xuwuyuan
     * @date: 2024/8/4 10:28
     * @desc: 插入套餐
     * @param setmealDTO
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
}
