package com.bnyte.encryption.bind;

import java.lang.annotation.*;

/**
 * 作用于方法：用于开启加密响应(必须加，如果请求的方法上没加的话就不会被加载，哪怕响应的对象属性上有@EncryptionField注解也不会被加载)
 *  如果Controller中的方法包含该注解则会被记录并被响应结果处理器进行处理属性加密
 *
 * @author bnyte
 * @date 2022/4/14 23:59
 * @mail bnytezz@gmail.com
 */
@Target(ElementType.METHOD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EnableFieldEncryption {
}
