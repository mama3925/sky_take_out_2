package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 地址簿接口
 * @createDate 2024/8/25 12:25
 **/
public interface AddressBookService {

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 12:35
     * @desc 通过输入封装对象列出满足要求的地址簿
     * @return java.util.List<com.sky.entity.AddressBook>
     **/
    List<AddressBook> list(AddressBook addressBook);

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 16:55
     * @desc 通过id寻找地址簿
     * @return com.sky.entity.AddressBook
     **/
    AddressBook findById(Long id);

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 17:01
     * @desc 通过id删除地址簿
     * @return void
     **/
    void removeById(Long id);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:07
     * @desc 插入地址
     * @return void
     **/
    void save(AddressBook addressBook);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:50
     * @desc 更新地址
     * @return void
     **/
    void update(AddressBook addressBook);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 20:29
     * @desc 设置默认地址
     * @return void
     **/
    void setDefault(AddressBook addressBook);
}
