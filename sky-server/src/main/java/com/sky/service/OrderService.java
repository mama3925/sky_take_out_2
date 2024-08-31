package com.sky.service;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 订单业务接口
 * @createDate 2024/8/26 19:54
 **/
public interface OrderService {

    /**
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderVO
     * @author xuwuyuan
     * @date 2024/8/26 21:33
     * @desc 用户新增订单
     **/
    OrderSubmitVO save(OrdersSubmitDTO ordersSubmitDTO);
}
