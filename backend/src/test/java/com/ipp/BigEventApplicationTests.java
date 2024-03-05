package com.ipp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class BigEventApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void testSet(){
        ValueOperations<String, String> option = stringRedisTemplate.opsForValue();

        option.set("username", "zhangsan");
        option.set("id", "1", 15, TimeUnit.SECONDS);

    }
    @Test
    public void testGet(){
        ValueOperations<String, String> option = stringRedisTemplate.opsForValue();
        System.out.println(option.get("username"));
        System.out.println(option.get("id"));
    }
}
