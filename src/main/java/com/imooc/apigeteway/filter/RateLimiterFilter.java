package com.imooc.apigeteway.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 *
 * 限流拦截器
 * 采用令牌限流：通过每秒放置的令牌数来限制请求数量，当1秒内超过指定的令牌数，就会抛出异常
 */
@Component
public class RateLimiterFilter extends ZuulFilter {

    //每秒放100个令牌
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1);
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        if (!RATE_LIMITER.tryAcquire()) {
            System.out.println("==========令牌限流开始==========");
            throw new RateLimiterException();
        }
        return null;
    }
}
