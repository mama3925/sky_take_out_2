package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author: xuwuyuan
 * @desc: 用户业务接口
 * @createDate: 2024/8/7 20:53
 **/
public interface UserService {

    /**
     * @author: xuwuyuan
     * @date: 2024/8/7 21:25
     * @desc: c端微信登录
     * @param userLoginDTO
     * @return: com.sky.entity.User
     */
    User WX_login(UserLoginDTO userLoginDTO);
}
