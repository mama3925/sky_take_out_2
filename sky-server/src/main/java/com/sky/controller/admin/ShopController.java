package com.sky.controller.admin;

import com.sky.config.RedisConfiguration;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuwuyuan
 * @desc: 管理端营业状态接口
 * @createDate: 2024/8/7 13:08
 **/
@RestController("AdminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "营业状态")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param status
     * @author: xuwuyuan
     * @date: 2024/8/7 13:39
     * @desc: 设置营业状态
     * @return: com.sky.result.Result
     */
    @PutMapping("/{status}") //这里忘了加大括号，所以导致路径无法识别
    @ApiOperation("设置营业状态")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置店铺状态为:{}", status == 1 ? "营业中" : "已打烊");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/7 13:40
     * @desc: 查询营业状态
     * @return: com.sky.result.Result
     */
    @GetMapping("/status")
    @ApiOperation("查询营业状态")
    public Result<Integer> getShopStatus() {
        log.info("查询店铺营业状态");
        return Result.success((Integer) redisTemplate.opsForValue().get(KEY));//直接调用业务层，然后并在一行写
    }
}
