package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 订单业务实现类
 * @createDate 2024/8/26 19:54
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderVO
     * @author xuwuyuan
     * @date 2024/8/26 21:41
     * @desc 新建订单
     **/
    @Override
    @Transactional
    public OrderSubmitVO save(OrdersSubmitDTO ordersSubmitDTO) {
        // 考虑地址为空，购物车为空的情况
        // 地址为空就抛错
        AddressBook addressBook = addressBookMapper.findById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 该用户是否存在购物车？
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart); // 寻找该用户对应的购物车

        // 构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(addressBook.getUserId());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        // 向订单表插入一条数据
        orderMapper.insert(order);

        // 订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 向明细表插入n条数据
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList); // 批量插入

        // 清理购物车中的数据
        shoppingCartMapper.removeByUserId(shoppingCart.getUserId());

        // 返回视图对象
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderTime(order.getOrderTime())
                .orderAmount(order.getAmount())
                .build();
    }

    /**
     * @param orderNumber
     * @author xuwuyuan
     * @date 2024/9/1 17:37
     * @desc 通过订单号结合用户id，向用户返回支付成功消息
     * @return void
     **/
    @Override
    public void paySuccess(String orderNumber) {
        // 使用数据查询实体
        Orders orderDB = orderMapper.findByOrderNumberAndUserId(orderNumber, BaseContext.getCurrentId());
        // 向数据库插入新值，改变支付状态为已支付，改变订单状态为待商家确认.最后确定支付时间为现在
        Orders order = Orders.builder()
                .id(orderDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(order);
    }
}
