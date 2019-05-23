package com.imooc.apigeteway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private Long expireTime = 60L;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping(value = "/loginvalidate")
    @ResponseBody
    public String login (@RequestParam String name,@RequestParam String password) {
        if (name.equals("admin")&&password.equals("123456")) {
            //记录token
            String token = tokenGen (name);
            return "login success!";
        } else {
            return "login fail!";
        }
    }

    public String tokenGen (String name) {
        String token = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println("token:"+token);
        //将token存储到redis
        redisTemplate.opsForValue().set(token,name);
        log.info(redisTemplate.opsForValue().get(token));
        return token;
    }
 }
