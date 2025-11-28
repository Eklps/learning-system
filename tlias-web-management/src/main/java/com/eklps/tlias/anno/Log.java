package com.eklps.tlias.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解，用于记录需要记录日志的操作
@Retention(RetentionPolicy.RUNTIME)//运行时有效
//是最重要的设置。它告诉 JVM：
// 这个注解信息不仅要在源代码和编译后的 .class 文件中保留，而且要在程序运行时通过反射机制仍然可以访问到。
@Target(ElementType.METHOD)//这个自定义Log作用限定在方法上
public @interface Log {
}
