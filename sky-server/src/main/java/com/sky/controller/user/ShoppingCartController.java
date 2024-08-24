package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 用户购物车
 * @createDate 2024/8/23 21:34
 **/

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "C端-购物车接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * @param shoppingCartDTO
     * @author xuwuyuan
     * @date 2024/8/23 21:52
     * @desc 添加购物车
     * @return com.sky.result.Result
     **/
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result save(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车, {}", shoppingCartDTO);
        shoppingCartService.save(shoppingCartDTO);
        return Result.success();
    }

    /**
     * @author xuwuyuan
     * @date 2024/8/24 15:48
     * @desc 查看购物车列表
     * @return com.sky.result.Result<java.util.List<com.sky.entity.ShoppingCart>>
     **/
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("c端查看购物车");
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        return Result.success(shoppingCartList);
    }
}
