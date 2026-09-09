package com.yatharth.distributedurlshortener.config;

import com.yatharth.distributedurlshortener.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String clientKey = getClientKey(request);

        if (!rateLimitService.isAllowed(clientKey)) {
            response.setStatus(429);
            response.setHeader("Retry-After", "60");
            return false;
        }

        return true;
    }

    private String getClientKey(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}