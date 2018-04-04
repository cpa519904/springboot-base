package com.company.common.security;

import com.company.service.Securityservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled=true)
//jsr250是最基本的，还有@prePostEnabled可以在方法执行前后拦截，支持自定义扩展
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private Securityservice securityservice;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //并根据传入的AuthenticationManagerBuilder中的userDetailsService方法来接收我们自定义的认证方法。
        //且该方法必须要实现UserDetailsService这个接口。
        auth.userDetailsService(securityservice)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/resources/**");
    }

    //2种方式，一种是url限制，另一种是用注解(注解默认是关闭的，要加@EnableGlobalMethodSecurity开启)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login")
                .permitAll();

        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        http.authorizeRequests()
                .and()
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .key("wsq");

        http.formLogin()
                .and()
                .httpBasic();

        http.logout()
                .permitAll();
    }
}
