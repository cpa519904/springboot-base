package com.company.common.security;

import com.company.common.exception.ExceptionCode;
import com.company.common.exception.ServiceException;
import com.company.dao.UserDao;
import com.company.pojo.entity.User;
import com.company.pojo.po.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
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

        //量大了，从redis读取，如果没读取到到数据库去读取，都没有则返回异常。

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

        //量大了加缓存策略，redis的map，username-token做key，expiryTime做value

        userDao.save(user);
    }

    private long calculateExpiryTime(int tokenLifetime) {
        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);
        return expiryTime;
    }
}
