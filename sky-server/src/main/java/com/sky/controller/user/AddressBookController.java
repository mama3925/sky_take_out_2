package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuwuyuan
 * @version 1.0
 * @Desc 地址簿功能
 * @createDate 2024/8/25 12:00
 **/
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "c端地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * @return com.sky.result.Result<java.util.List < com.sky.entity.AddressBook>>
     * @author xuwuyuan
     * @date 2024/8/25 12:33
     * @desc 列出地址簿
     **/
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        log.info("c端列出当前用户的地址簿项");
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return Result.success(addressBookService.list(addressBook));
    }

    /**
     * @return com.sky.result.Result<com.sky.entity.AddressBook>
     * @author xuwuyuan
     * @date 2024/8/25 13:57
     * @desc 查询默认地址
     **/
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> findDefault() {
        // 需要同时控制用户id和默认与否属性
        log.info("c端查询默认地址");
        // 错误，我的代码没有控制用户id和无默认的情况 return Result.success(addressBookService.findDefault());
        AddressBook addressBook = AddressBook.builder()
                .userId(BaseContext.getCurrentId())
                .isDefault(1)
                .build();
        List<AddressBook> addressBookList = addressBookService.list(addressBook);
        if (addressBookList == null || addressBookList.size() == 0) {
            throw new BaseException("无默认地址");
        } else if (addressBookList != null && addressBookList.size() > 1) {
            throw new BaseException("错误！存在多个默认地址");
        }
        return Result.success(addressBookList.get(0));
    }

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 16:53
     * @desc 根据id查询地址
     * @return com.sky.result.Result<com.sky.entity.AddressBook>
     **/
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> findById(@PathVariable Long id) {
        log.info("c端查询地址:{}", id);
        return Result.success(addressBookService.findById(id));
    }

    /**
     * @param id
     * @author xuwuyuan
     * @date 2024/8/25 17:00
     * @desc 根据id删除地址
     * @return com.sky.result.Result
     **/
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result removeById(Long id) {
        log.info("c端删除地址:{}",id);
        addressBookService.removeById(id);
        return Result.success();
    }

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:06
     * @desc 通过实体插入地址
     * @return com.sky.result.Result
     **/
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        log.info("c端新增地址");
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * @param addressBook
     * @author xuwuyuan
     * @date 2024/8/25 17:47
     * @desc 编辑地址
     * @return com.sky.result.Result
     **/
    @PutMapping
    @ApiOperation("编辑地址")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("c端编辑地址");
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * @author xuwuyuan
     * @date 2024/8/25 20:15
     * @desc 设置默认地址
     * @return com.sky.result.Result
     **/
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        log.info("c端设置默认地址:{}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }
}
