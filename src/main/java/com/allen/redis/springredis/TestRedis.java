package com.allen.redis.springredis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void testRedis() {
        stringRedisTemplate.opsForValue().set("testkey","vadfad");
        System.out.println(stringRedisTemplate.opsForValue().get("testkey"));

        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.put("hash01","name","oooo");
        hash.put("hash01","age","22");
        System.out.println(hash.entries("hash01"));
        Person p = new Person();
        p.setName("zhangsn");
        p.setAge(17);
        // 存储
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper,false);
        stringRedisTemplate.opsForHash().putAll("hash03",jm.toHash(p));
        // 还原
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("hash03");
        Person person = objectMapper.convertValue(map, Person.class);
        System.out.println(person.getName());
    }
}


