package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuwuyuan
 * @desc: c端接口
 * @createDate: 2024/8/7 20:04
 **/
@RestController
@RequestMapping("/user/user")
@Api(tags = "用户端接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * @author: xuwuyuan
     * @date: 2024/8/7 21:02
     * @desc: 用户登录
     * @param userLoginDTO
     * @return: com.sky.result.Result<com.sky.vo.UserLoginVO>
     */
    @PostMapping("/login")
    @ApiOperation("c端登录")
    public Result<UserLoginVO> WX_login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("c端用户登录:{}", userLoginDTO);
        User user = userService.WX_login(userLoginDTO);//这个用户对象只有id openid属性，后续还要加上token，合并为VO对象

        //创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        //将token加入user对象形成userLoginVO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/9 12:46
     * @desc: c端用户登录退出
     * @return: com.sky.result.Result
     */
    @PostMapping("/logout")
    @ApiOperation("退出")
    public Result WX_logout() {
        log.info("c端用户退出");
        return Result.success();
    }
}

