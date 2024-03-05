package com.ipp.controller;

import com.ipp.pojo.Result;
import com.ipp.pojo.User;
import com.ipp.service.UserService;
import com.ipp.utils.JwtUtils;
import com.ipp.utils.Md5Util;
import com.ipp.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    // 用户注册
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username,
                           @Pattern(regexp = "^\\S{5,16}$") String password){
        // 查询用户
        User user = userService.findByUsername(username);
        // 无用户信息，注册新用户
        if (user == null){
            userService.register(username, password);
            return Result.success();
        }else {
            // 有用户信息，返回已注册
            return Result.error("用户名已注册");
        }
    }

    // 用户登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$")String username,
                        @Pattern(regexp = "^\\S{5,16}$") String password){

        // 根据用户名查询用户
        User loginUser = userService.findByUsername(username);
        // 判断用户是否存在
        if (loginUser != null){
            // 有结果，验证密码
            if (Md5Util.getMD5String(password).equals(loginUser.getPassword())){
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", loginUser.getId());
                claims.put("username", loginUser.getUsername());
                String token = JwtUtils.generateJwt(claims);
                // 将token存储到redis中
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                ops.set(token, token, 1, TimeUnit.HOURS);
                return Result.success(token);
            }
            // 密码错误，返回登陆失败
            return Result.error("密码错误");
        }
        // 用户不存在
        return Result.error("用户名错误");
    }

    @GetMapping("/userInfo")
    // 根据用户名获取用户详细信息
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
//        Map<String, Object> claims = JwtUtils.parseJWT(token);
//        String username = (String) claims.get("username");
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    // 更新用户信息
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    // 更新用户头像
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    // 更新密码
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token){
        // 参数校验
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数");
        }
        // 原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUsername(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码填写错误");
        }
        // 两次密码填写是否相同
        if (!rePwd.equals(newPwd)){
            return Result.error("两次密码填写不一致");
        }
        // 调用service完成密码更新
        userService.updatePwd(newPwd);
        // 删除redis中的旧token
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);
        return Result.success();
    }
}
