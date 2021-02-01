package com.allen.redis.springredis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TestRedis {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("testTemplate")
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void testRedis() {

        /**
         * 字符串存储
         * 2021-02-01 18:29:16
         */
        redisTemplate.opsForValue().set("rediskey", "123123123");
        System.out.println(redisTemplate.opsForValue().get("rediskey"));
        // 简单存储和读取
        stringRedisTemplate.opsForValue().set("testkey","vadfad");
        System.out.println(stringRedisTemplate.opsForValue().get("testkey"));

        // hash存储
        HashOperations<String, Object, Object> hash = stringRedisTemplate.opsForHash();
        hash.put("hash01","name","oooo");
        hash.put("hash01","age","22");
        System.out.println(hash.entries("hash01"));
        Person p = new Person();
        p.setName("zhangsn");
        p.setAge(17);

        /**
         * hash存储
         * 2021-02-01 18:29:06
         */
        // 存储
        //stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper,false);
        stringRedisTemplate.opsForHash().putAll("hash03",jm.toHash(p));
        // 还原
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("hash03");
        Person person = objectMapper.convertValue(map, Person.class);
        System.out.println(person.getName());

        stringRedisTemplate.convertAndSend("me","hello");
        System.out.println(" send ok ");

        /**
         * 发布订阅
         * 2021-02-01 18:28:53
         */
        RedisConnection cc = stringRedisTemplate.getConnectionFactory().getConnection();
        cc.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] bytes) {
                byte[] body = message.getBody();
                String str = new String(body);
                System.out.println("收到消息："+str);
            }
        }, "me".getBytes());

        while (true) {
            stringRedisTemplate.convertAndSend("me","我消息给你，你能收到吗？");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


