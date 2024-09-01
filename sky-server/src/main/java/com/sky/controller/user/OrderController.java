package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 订单控制类
 * @createDate 2024/8/26 19:51
 **/
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @param ordersSubmitDTO
     * @author xuwuyuan
     * @date 2024/8/31 9:57
     * @desc c端下单
     * @return com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     **/
    @ApiOperation("用户下单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> userSubmit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("c端用户下单:{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.save(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * @param ordersPaymentDTO
     * @author xuwuyuan
     * @date 2024/9/1 17:26
     * @desc 订单支付功能
     * @return com.sky.result.Result<java.lang.String>
     **/
    @ApiOperation("订单支付")
    @PutMapping("/payment")
    public Result<String> userPayment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("订单支付:{}", ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success();
    }
}
