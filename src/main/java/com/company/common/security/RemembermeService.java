package com.company.common.security;

import com.company.common.exception.ServiceException;
import com.company.dao.UserDao;
import com.company.pojo.entity.User;
import com.company.pojo.po.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class RemembermeService extends TokenBasedRememberMeServices{
    @Autowired
    private UserDao userDao;

    public RemembermeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        if (cookieTokens.length == 0) {
            //throw new ServiceException();
        }

        User user = userDao.findByToken(cookieTokens[0]);

        if (isTokenExpired(user.getExpiryTime())) {
            //throw new ServiceException();
        }

        return new SecurityUser(user);
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String username = retrieveUserName(successfulAuthentication);

        if (!StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }

        UserDetails userDetails = getUserDetailsService().loadUserByUsername(username);

        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();

        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);
        String token = UUID.randomUUID().toString();

        setCookie(new String[] { token }, tokenLifetime, request, response);

        User user = ((SecurityUser)userDetails).getUser();
        user.setToken(token);
        user.setExpiryTime(expiryTime);

        userDao.save(user);
    }
}
