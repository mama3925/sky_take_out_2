package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc
 * @createDate 2024/8/23 21:50
 **/
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * @param shoppingCartDTO
     * @return void
     * @author xuwuyuan
     * @date 2024/8/23 21:54
     * @desc 插入购物车
     **/
    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);// 将视图对象拷贝到封装对象中，进而查询数据库
        shoppingCart.setUserId(BaseContext.getCurrentId());// 约束当前用户id，以便查询。确保只查询用户自己的数据

        // 判断当前产品是否在购物车中
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        if (shoppingCarts != null && shoppingCarts.size() == 1) {
            // 当前购物车对象已经存在，为了避免重复，所以number属性++。
            shoppingCart = shoppingCarts.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumber(shoppingCart);
        } else {
            // 不存在的情况下，新建并插入一条记录
            Long dishId = shoppingCart.getDishId();

            // 该条记录是菜品，或是套餐
            if (dishId != null) {
                // 该记录是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
            } else {
                // 该记录是套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.save(shoppingCart);
        }
    }

    /**
     * @return java.util.List<com.sky.entity.ShoppingCart>
     * @author xuwuyuan
     * @date 2024/8/24 15:49
     * @desc 查看购物车列表实现类
     **/
    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list(ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build());
    }

    /**
     * @author xuwuyuan
     * @date 2024/8/24 16:06
     * @desc 清空购物车
     * @return void
     **/
    @Override
    public void cleanCart() {
        shoppingCartMapper.removeByUserId(BaseContext.getCurrentId());
    }
}
