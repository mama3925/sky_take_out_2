package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.AddressBook;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 地址簿数据库操作
 * @createDate 2024/8/25 12:27
 **/
@Mapper
public interface AddressBookMapper {

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 12:38
     * @desc 查询数据库
     * @return java.util.List<com.sky.entity.AddressBook>
     **/
    List<AddressBook> list(AddressBook addressBook);

    // 我的方法不好，还要开两个查询。他只要一个条件查询，返回列表。然后就可以通用，同时实现当前用户地址列表查询和当前用户
//    @Select("select * from address_book where is_default = #{isDefault}")
//    List<AddressBook> findDefault(AddressBook addressBook);

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 16:57
     * @desc 通过id找到地址簿实体
     * @return com.sky.entity.AddressBook
     **/
    @Select("select * from address_book where id = #{id}")
    AddressBook findById(Long id);

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 17:09
     * @desc 根据id删除地址
     * @return void
     **/
    @Delete("delete from address_book where id = #{id}")
    void removeById(Long id);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:09
     * @desc 根据实体插入地址
     * @return void
     **/
    // @AutoFill(OperationType.INSERT)
    @Insert("insert into address_book (user_id, consignee, phone, sex, province_code, province_name, " +
            "city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{phone},#{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, " +
            "#{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void save(AddressBook addressBook);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:51
     * @desc 更新地址
     * @return void
     **/
    // @AutoFill(OperationType.UPDATE)
    void update(AddressBook addressBook);

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 20:44
     * @desc 将该用户的所有地址设为非默认
     * @return void
     **/
    @Update("update address_book set user_id = #{userId}, is_default = #{isDefault}")
    void setAllToNotDefault(AddressBook addressBook);
}
