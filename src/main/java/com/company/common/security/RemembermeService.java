package com.company.common.security;

import com.company.common.exception.ExceptionCode;
import com.company.common.exception.ServiceException;
import com.company.dao.UserDao;
import com.company.pojo.entity.User;
import com.company.pojo.po.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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

@Configuration
public class RemembermeService extends TokenBasedRememberMeServices {
    @Autowired
    private UserDao userDao;

    public RemembermeService(UserDetailsService userDetailsService) {
        super("rememberMe", userDetailsService);
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        //校验
        if (cookieTokens.length != 2) {
            throw new ServiceException(ExceptionCode.NEED_LOGIN.getCode(), "cookie丢失");
        }

        User user = userDao.findByUsernameAndToken(cookieTokens[0], cookieTokens[1]);

        if (user == null) {
            throw new ServiceException(ExceptionCode.NEED_LOGIN.getCode(), "token无效");
        }

        if (isTokenExpired(user.getExpiryTime())) {
            throw new ServiceException(ExceptionCode.NEED_LOGIN.getCode(), "token已过期");
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
        long expiryTime = calculateExpiryTime(tokenLifetime);
        String token = UUID.randomUUID().toString();

        setCookie(new String[]{username, token}, tokenLifetime, request, response);

        User user = ((SecurityUser) userDetails).getUser();
        user.setToken(token);
        user.setExpiryTime(expiryTime);

        userDao.save(user);
    }

    private long calculateExpiryTime(int tokenLifetime) {
        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);
        return expiryTime;
    }
}
