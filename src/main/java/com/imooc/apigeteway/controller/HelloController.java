package com.imooc.apigeteway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping(value = "/local")
    @ResponseBody
    public String hello () {
        System.out.println("hello zuul");
        return "hello zuul";
    }
}
