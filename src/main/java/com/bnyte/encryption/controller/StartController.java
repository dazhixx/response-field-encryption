package com.bnyte.encryption.controller;

import com.bnyte.encryption.bind.EnableFieldEncryption;
import com.bnyte.encryption.vo.User;
import com.bnyte.forge.http.reactive.web.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bnyte
 * @date 2022/4/15 1:03
 * @mail bnytezz@gmail.com
 */
@RestController
@RequestMapping("/start")
public class StartController {

    /**
     * 不加密，请求地址：localhost:8080/start/original
     * @return 响应结果
     */
    @GetMapping("/original")
    R<User> original() {
        return R.ok(User.getInstance());
    }

    /**
     * 加密，请求地址：localhost:8080/start/encryption
     * @return 响应结果
     */
    @EnableFieldEncryption // 开启加密
    @GetMapping("/encryption")
    R<User> encryption() {
        return R.ok(User.getInstance());
    }

}
