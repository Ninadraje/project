package com.project.linkedIn.post_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;


/**
 * This Class will add the request header in the outgoing request to feign client
 * Also this class can be used to add anything in the outgoing request through feign
 * */

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
