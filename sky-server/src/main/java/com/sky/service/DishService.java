package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.User;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 菜品业务接口
 * @create: 2024/7/29 17:53
 **/
public interface DishService {

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/7/29 20:40
     * @desc: 新增菜品实现接口
     * @return: void
     */
    void save(DishDTO dishDTO);

    /**
     * @param dishPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/30 10:17
     * @desc: 菜品分页查询
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @param ids
     * @author: xuwuyuan
     * @date: 2024/7/31 8:38
     * @desc: 批量删除菜品
     * @return: void
     */
    void deleteBatch(List<Long> ids);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/31 12:24
     * @desc: 获取dishvo对象。相比于dish对象，dishvo增加了属性flavors列表
     * @return: com.sky.vo.DishVO
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * @param dishDTO
     * @author: xuwuyuan
     * @date: 2024/8/3 15:23
     * @desc: 修改菜品功能
     * @return: void
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/8/3 20:37
     * @desc: 启停用功能
     * @return: void
     */
    void startOrStop(Integer status, Long id);

    /**
     * @param CategoryId
     * @author: xuwuyuan
     * @date: 2024/8/5 11:23
     * @desc: 返回菜品列表
     * @return: java.util.List<com.sky.entity.Dish>
     */
    List<Dish> listByCategoryId(Long CategoryId);

    //////以下都是用户端的新增业务接口
    /**
     * @author: xuwuyuan
     * @date: 2024/8/8 10:58
     * @desc: 返回带口味的菜品列表
     * @param dish
     * @return: java.util.List<com.sky.vo.DishVO>
     */
    List<DishVO> listByCategoryIdWithFlavor(Dish dish);
}
