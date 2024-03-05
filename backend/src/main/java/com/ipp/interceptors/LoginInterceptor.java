package com.ipp.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.ipp.pojo.Result;
import com.ipp.utils.JwtUtils;
import com.ipp.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String token = request.getHeader("Authorization");
        // 存在，解析token
        try {
            // 从redis中获取相同的token
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String redisToken = ops.get(token);
            if (redisToken == null){
                // token为空，则表示已失效（用户修改密码...）
                throw new RuntimeException(); // 抛出异常，catch捕捉
            }
            Map<String, Object> claims = JwtUtils.parseJWT(token);
            // token中业务数据存储到ThreadLocal
            ThreadLocalUtil.set(claims);
            // 解析成功，放行
            return true;
        } catch (Exception e) {
            // 解析失败返回错误结果
            response.setStatus(401);
            Result error = Result.error("not login");
            // 转换为json
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
