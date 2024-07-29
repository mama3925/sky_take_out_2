package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author: xuwuyuan
 * @desc: 通用接口，用于图片上传
 * @create: 2024/7/29 16:32
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * @author: xuwuyuan
     * @date: 2024/7/29 16:59
     * @desc: 图片上传功能
     * @param file
     * @return: com.sky.result.Result<java.lang.String>
     */
    @PostMapping("/upload")
    @ApiOperation("图片上传")
    public Result<String> uploadImage(@RequestBody MultipartFile file) {
        log.info("文件上传功能开始:{}", file);
        try{
            String originalFilename = file.getOriginalFilename();
            String postfix = originalFilename.substring(originalFilename.lastIndexOf('.'));//例如：对于caonima.txt这个文件，我们只取.txt作为后缀
            String newFilename = UUID.randomUUID() + postfix;//新文件名等于随机uuid加上一行的后缀名
            String result = aliOssUtil.upload(file.getBytes(), newFilename);//文件转为字节，然后附上新文件名，传入接口。他就能自动返回图片url链接
            return Result.success(result);
        } catch (IOException e) {//这里犯错了，类型是IOException，因为涉及Java IO。
            log.info("文件上传失败:{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED); //这里最好用常量
    }

}
