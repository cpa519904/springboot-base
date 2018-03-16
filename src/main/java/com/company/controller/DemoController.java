package com.company.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("base")
public class DemoController {

    @RequestMapping("/demo")
    public String demo() {

//        User user  = new User();
//        user.setId(1);
//        user.setUsername("shdjak");

        return "123";
    }
}
