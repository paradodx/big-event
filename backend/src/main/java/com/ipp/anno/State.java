package com.ipp.anno;

import com.ipp.validation.StateVadation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented // 元注解
@Constraint(validatedBy = {StateVadation.class})
@Target({FIELD}) // 元注解 注解使用
@Retention(RUNTIME) // 元注解
public @interface State {
    // 提供校验失败信息
    String message() default "state参数的值仅为 已发布/草稿";
    // 指定分组
    Class<?>[] groups() default { };
    // 负载 获取到state注解的附加信息
    Class<? extends Payload>[] payload() default { };
}
