package com.bnyte.encryption.vo;

import com.bnyte.encryption.bind.EncryptionField;
import com.bnyte.encryption.enums.EEncryptionType;

import java.util.List;

/**
 * @author bnyte
 * @date 2022/4/15 1:04
 * @mail bnytezz@gmail.com
 */
public class User {

    public static User getInstance() {
        return new User("ggboy_username", "ggboy_password", "18888888888", List.of(
                new User("ggboy1_inner_username", "ggboy1_inner_username", "18888888888"),
                new User("ggboy1_inner_username", "ggboy1_inner_username", "18888888888", List.of(
                        new User("ggboy1_inner_username", "ggboy1_inner_username", "18888888888"),
                        new User("ggboy1_inner_username", "ggboy1_inner_username", "18888888888")
                ))
        ));
    }

    private String username;

    @EncryptionField // 属性有这个注解的话就会被加密
    private String password;

    @EncryptionField(type = EEncryptionType.MOBILE) // 指定手机加密
    private String mobile;

    private List<User> users;

    public User() {
    }

    public User(String username, String password, String mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public User(String username, String password, String mobile, List<User> users) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.users = users;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, List<User> users) {
        this.username = username;
        this.password = password;
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
