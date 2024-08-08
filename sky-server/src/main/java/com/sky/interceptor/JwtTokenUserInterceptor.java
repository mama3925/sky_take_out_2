package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: xuwuyuan
 * @desc: 自定义用户拦截器
 * @createDate: 2024/8/8 9:13
 **/
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * @param request
     * @param response
     * @param handler
     * @author: xuwuyuan
     * @date: 2024/8/8 9:25
     * @desc: 用户登录拦截器
     * @return: boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;//当前拦截到的不是动态方法，直接放行
        String token = request.getHeader(jwtProperties.getUserTokenName());//从请求头中获取token

        //校验令牌
        try {
            log.info("用户端jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);//得到jwt的claims
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            ///////////////////////////////////////////////////////////
            BaseContext.setCurrentId(userId);//给localthread保存一个c端用户id
            ///////////////////////////////////////////////////////////
            log.info("当前员工id:{}", userId);
            return true; //放行
        } catch (Exception e) {
            //不通过，相应404状态码
            response.setStatus(404);
            return false; //拦截
        }
    }
}
