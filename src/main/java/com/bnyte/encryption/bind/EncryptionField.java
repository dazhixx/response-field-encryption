package com.bnyte.encryption.bind;

import com.bnyte.encryption.enums.EEncryptionType;
import org.springframework.util.StringUtils;

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

    String value() default "";

    /**
     * 自定义当前属性加密方式
     * @return 加密类型
     */
    EEncryptionType type() default EEncryptionType.DEFAULT;
}
