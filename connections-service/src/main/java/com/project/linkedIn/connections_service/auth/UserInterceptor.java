package com.project.linkedIn.connections_service.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/*
* This will Intercept all the incoming requests.
* From the request we will get the X-User-Id header and from
* that header we will call the UserContextHolder to get the userId.
*
* This is created, so we do not have to change the controller method
* parameters
* */

@Component
public class UserInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId= request.getHeader("X-User-Id");

        if(null != userId){
            UserContextHolder.setCurrentUserId(Long.valueOf(userId));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        UserContextHolder.clear();

    }
}
