package com.company.common.aop;

import com.company.common.exception.ExceptionCode;
import com.company.common.exception.ServiceException;
import com.company.common.exception.SystemException;
import com.company.common.tools.SignUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

@Aspect
@Component
public class VerifySign {
    private Logger logger = LoggerFactory.getLogger("VerifySign");

    @Value("${secretKey}")
    private String secretKey;

    @Pointcut("@annotation(com.company.common.annotations.CheckSign)")
    public void aspect() {
    }

    @Before("aspect()")
    public void before(JoinPoint joinPoint) {
        String sign;
        String trueSign = null;
        HttpServletRequest request = null;
        Map<String, String> map = new TreeMap<>();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
                trueSign = request.getParameter("sign");
            }
        }

        if (StringUtils.isEmpty(trueSign)) {
            throw new ServiceException(ExceptionCode.SIGN_ERROR.getCode(), "sign缺失");
        }

        for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            String key = e.nextElement().toString();
            String value = request.getParameter(key);
            map.put(key, value);
        }

        try {
            sign = SignUtil.generateSign(secretKey, map);
        } catch (Exception e) {
            logger.error("验签异常", e);
            throw new SystemException("验签异常");
        }


        if (!sign.equalsIgnoreCase(trueSign)) {
            logger.error("sign错误");
            throw new ServiceException(ExceptionCode.SIGN_ERROR.getCode(), "sign错误");
        }

    }
}
