package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 购物车接口
 * @createDate 2024/8/23 21:49
 **/
public interface ShoppingCartService {
    /**
     * @param shoppingCartDTO
     * @author xuwuyuan
     * @date 2024/8/23 21:54
     * @desc 插入购物车项
     * @return void
     **/
    void save(ShoppingCartDTO shoppingCartDTO);

    /**
     * @author xuwuyuan
     * @date 2024/8/24 15:49
     * @desc 查看购物车列表
     * @return java.util.List<com.sky.entity.ShoppingCart>
     **/
    List<ShoppingCart> list();
}
