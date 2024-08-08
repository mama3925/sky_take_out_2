package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: xuwuyuan
 * @desc: 用户持久层
 * @createDate: 2024/8/7 20:55
 **/
@Mapper
public interface UserMapper {

    /**
     * @author: xuwuyuan
     * @date: 2024/8/8 8:52
     * @desc: 根据openId来选择用户实体
     * @param openid
     * @return: com.sky.entity.User
     */
    @Select("select * from user where openid = #{openid}")
    User getUserByOpenId(String openid);

    /**
     * @author: xuwuyuan
     * @date: 2024/8/8 8:54
     * @desc: 用户插入
     * @param user
     * @return: void
     */
    void insert(User user);
}
