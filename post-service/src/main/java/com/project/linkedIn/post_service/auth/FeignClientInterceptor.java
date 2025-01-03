package com.project.linkedIn.post_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        Long userId= UserContextHolder.getCurrentUserId();
        if(null!=userId){
            requestTemplate.header("X-User-Id", String.valueOf(userId));
        }
    }
}
