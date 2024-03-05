package com.ipp.mapper;


import com.ipp.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper{

    // 根据用户名查询用户
    @Select("select id, username, password, nickname, email, user_pic, create_time, update_time from user where " +
            "username = #{username}")
    User findByUsername(String username);

    // 添加用户
    @Insert("insert into user(username, password, create_time, update_time)" +
            " values(#{username}, #{password}, now(), now())")
    void add(String username, String password);

    // 修改用户信息
    @Update("update user set nickname=#{nickname}, email=#{email}, update_time=#{updateTime} where id = #{id}")
    void update(User user);

    // 修改用户图片
    @Update("update user set user_pic=#{avatarUrl}, update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl, Integer id);

    // 修改用户密码
    @Update("update user set password=#{md5String}, update_time=now() where id=#{id}")
    void updatePwd(String md5String, Integer id);
}
