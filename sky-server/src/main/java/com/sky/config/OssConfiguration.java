package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuwuyuan
 * @desc: aliOssUtil工具类的配置类，实现ioc功能
 * @create: 2024/7/29 17:09
 **/
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * @param aliOssProperties
     * @author: xuwuyuan
     * @date: 2024/7/29 17:17
     * @desc: 在配置类中使用@Bean注解，也可以实现@Component功能。这里是为了构造方法传参，没办法了。如果用@Component，只能调用无参构造方法。
     * @return: com.sky.utils.AliOssUtil
     */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云oss文件工具类对象，四个参数为:{}", aliOssProperties);//我漏了这句话，以后记住配置类也需要日志。不能只添加控制类日志

        //虽然AliOssUtil类里面没有显性构造方法，但是类上方有一个@AllArgsConstructor注解，可自动生成全参构造器。
        return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());
    }

}