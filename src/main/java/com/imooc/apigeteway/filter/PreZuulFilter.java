package com.imooc.apigeteway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreZuulFilter extends ZuulFilter {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURI();
        //获取认证名称
        String authName = request.getHeader("Authorization");
        String token = null;
        if (authName!=null && !authName.equals("")) {
            //用户请求时会在请求头Authorization传给我之前设置的token
            authName = authName.replaceAll("Bearer ","");
            //获取redis存储的token
            String token1 = redisTemplate.opsForValue().get(authName).toString();
            System.out.println(token1);
            if (redisTemplate.opsForValue().get(authName).toString()!=null) {
                System.out.println(token);
                token = redisTemplate.opsForValue().get(authName).toString();
            }
        }
        if (token==null) {
            context.setResponseStatusCode(401);
            context.setSendZuulResponse(false);
            context.setResponseBody("{\"code\":401,\"msg\":\"令牌失效,请重新登录！\"}");
            context.getResponse().setContentType("text/html;charset=UTF-8");
        }


        return null;
    }
}
