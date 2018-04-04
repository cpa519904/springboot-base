package com.company.controller;

import com.company.pojo.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("base")
public class DemoController {

    @RequestMapping("/demo")
    @RolesAllowed("ADMIN")
    public User demo() {

        User user  = new User();
        user.setId(1);
        user.setUsername("shdjak");

        return user;
    }
}
