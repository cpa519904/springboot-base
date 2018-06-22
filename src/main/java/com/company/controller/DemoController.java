package com.company.controller;

import com.company.common.annotations.UncheckToken;
import com.company.common.exception.ExceptionCode;
import com.company.common.exception.ServiceException;
import com.company.common.tools.Constants;
import com.company.common.tools.Utils;
import com.company.common.tools.redis.RedisHelper;
import com.company.dao.UserDao;
import com.company.pojo.entity.CurrentUser;
import com.company.pojo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("base")
public class DemoController {
    @Value("${rememberMe.time}")
    private String rememberMeTime;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisHelper redisHelper;

    @RequestMapping("login")
    @UncheckToken
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDao.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new ServiceException(ExceptionCode.USERNAME_OR_PASSWORD_ERROR.getCode(), "用户名密码错误");
        } else {
            String key = UUID.randomUUID().toString();
            CurrentUser currentUser = new CurrentUser();
            BeanUtils.copyProperties(user, currentUser);
            redisHelper.writeObject(key, currentUser, Long.parseLong(rememberMeTime));
            Utils.setCookie(response, Constants.TOKEN, key, Integer.parseInt(rememberMeTime));
        }

        return "登入成功";
    }

    @RequestMapping("demo")
    @RolesAllowed("ROLE_ADMIN")
    public User demo(@RequestParam("zh") String zh) {

        User user = new User();
        user.setId(1);
        user.setUsername(zh);

        return user;
    }
}
