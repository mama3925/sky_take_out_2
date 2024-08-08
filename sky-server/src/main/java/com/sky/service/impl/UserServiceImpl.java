package com.sky.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.HttpUtil;
import com.google.gson.JsonObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xuwuyuan
 * @desc: 用户业务实现类
 * @createDate: 2024/8/7 20:53
 **/
@Service
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * @param userLoginDTO
     * @author: xuwuyuan
     * @date: 2024/8/7 21:25
     * @desc: c端微信登录实现类
     * @return: com.sky.entity.User
     */
    @Override
    public User WX_login(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();//获取c端过来的授权码

        //调用微信远端接口，尝试登录
        String openId = getOpenId(code);
        if (openId == null)
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);//不存在openId，说明微信远端接口没有返回，微信登录失败，所以外卖app也不能给他登

        //检查用户是否已经在系统中注册，如果没有注册，那就新创建一个
        User user = userMapper.getUserByOpenId(openId);
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);//插入创建好的用户对象
        }
        return user;
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/8/7 22:14
     * @desc: 调用微信接口服务，返回当前用户的openId
     * @param code
     * @return: java.lang.String
     */
    private String getOpenId(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "autorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
