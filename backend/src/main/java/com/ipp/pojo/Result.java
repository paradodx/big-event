package com.ipp.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private T data; //返回的数据

    //不带响应数据
    public static Result success() {
        return new Result(1, "success", null);
    }
    //带响应数据
    public static<E> Result<E> success(E data){
        return new Result<>(1,"success",data);
    }
    //失败响应
    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
