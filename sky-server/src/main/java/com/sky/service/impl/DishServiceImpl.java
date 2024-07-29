package com.sky.service.impl;

import com.sky.controller.admin.DishController;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * @author: xuwuyuan
     * @date: 2024/7/29 20:41
     * @desc: 新增菜品实现类
     * @param dishDTO
     * @return: void
     */
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
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

}
