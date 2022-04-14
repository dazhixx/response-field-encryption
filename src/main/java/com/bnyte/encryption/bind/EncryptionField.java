package com.bnyte.encryption.bind;

import java.lang.annotation.*;

/**
 * 作用域属性上，被标识的属性则会被扫描，并且通过自己的加密逻辑进行加密
 *
 * @author bnyte
 * @date 2022/4/15 0:03
 * @mail bnytezz@gmail.com
 */
@Target(ElementType.FIELD)
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EncryptionField {
}
