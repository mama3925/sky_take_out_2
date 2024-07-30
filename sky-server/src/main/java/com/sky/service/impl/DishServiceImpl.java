package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.controller.admin.DishController;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.FieldMissingException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
        //先对DTO对象的categoryId属性做一个限制，如果为空，直接报错。这个是源码里没有的内容，但实际上前端代码里对分类id属性做了限制，不用我这里多此一举。
        if (dishDTO.getCategoryId() == null) throw new FieldMissingException("操你妈！categoryId属性为什么不给？");

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

    /**
     * @author: xuwuyuan
     * @date: 2024/7/30 10:18
     * @desc: 菜品分页查询
     * @param dishPageQueryDTO
     * @return: com.sky.result.PageResult
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
