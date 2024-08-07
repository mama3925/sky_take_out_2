package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: xuwuyuan
 * @desc: Redis配置类，确保新生成的对象是StringSerializer,不是keySerial...Serializer
 * @createDate: 2024/8/7 13:11
 **/
@Configuration
@Slf4j
public class RedisConfiguration {

    /**
     * @param redisConnectionFactory
     * @author: xuwuyuan
     * @date: 2024/8/7 13:21
     * @desc: 第三方类的ioc功能，无法用@Component
     * @return: org.springframework.data.redis.core.RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始初始化Redis模板:{}", redisConnectionFactory);
        RedisTemplate redisTemplate = new RedisTemplate();//新建一个对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);//设置连接工厂对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());//将默认自动装配的序列器改为StringRedis序列器
        return redisTemplate;
    }
}
