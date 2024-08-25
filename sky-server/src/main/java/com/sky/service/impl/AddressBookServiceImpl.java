package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 地址簿业务实现类
 * @createDate 2024/8/25 12:26
 **/
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * @param addressBook
     * @return java.util.List<com.sky.entity.AddressBook>
     * @author xuwuyuan
     * @date 2024/8/25 12:35
     * @desc 通过输入封装对象列出满足要求的地址簿
     **/
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 16:55
     * @desc 通过id查找地址
     * @return com.sky.entity.AddressBook
     **/
    @Override
    public AddressBook findById(Long id) {
        return addressBookMapper.findById(id);
    }

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 17:01
     * @desc 通过id删除地址
     * @return void
     **/
    @Override
    public void removeById(Long id) {
        addressBookMapper.removeById(id);
    }

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:08
     * @desc 新增地址
     * @return void
     **/
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());// 设置用户id为当前c端登录用户
        addressBook.setIsDefault(0);// 新增地址都是非默认
        addressBookMapper.save(addressBook);
    }

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:50
     * @desc 编辑地址
     * @return void
     **/
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 20:37
     * @desc 设置默认地址
     * @return void
     **/
    @Override
    public void setDefault(AddressBook addressBook) {
        // 所有地址都设为非默认
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.setAllToNotDefault(addressBook);

        //当前id的地址设为默认
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
