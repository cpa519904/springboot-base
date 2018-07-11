package com.company.listen;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class Listener implements ApplicationListener<FirstEvent>{
    @Override
    public void onApplicationEvent(FirstEvent event) {
        System.out.println(event.getN());
    }
}
