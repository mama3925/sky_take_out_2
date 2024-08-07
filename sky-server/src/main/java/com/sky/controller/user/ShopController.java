package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuwuyuan
 * @desc: 用户端营业状态管理
 * @createDate: 2024/8/7 13:47
 **/
@RestController("UserShopController")
@RequestMapping("/user/shop")
@Api(tags = "用户营业状态管理")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/7 13:51
     * @desc: 获取营业状态
     * @return: com.sky.result.Result<java.lang.Integer>
     */
    @GetMapping("/status")
    @ApiOperation("用户端营业查询")
    public Result<Integer> getShopStatus() {
        log.info("用户端获取营业状态");
        return Result.success((Integer) redisTemplate.opsForValue().get(KEY));
    }
}
