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
        return new User("ggboy_username", 123, 321, List.of(
                new User("ggboy1_inner_username", 123, 321),
                new User("ggboy1_inner_username", 123, 321, List.of(
                        new User("ggboy1_inner_username", 123, 321),
                        new User("ggboy1_inner_username", 123, 321)
                ))
        ), new User("加密对象", 21321));
    }

    private String username;

    @EncryptionField // 属性有这个注解的话就会被加密
    private Integer password;

    @EncryptionField(type = EEncryptionType.MOBILE) // 指定手机加密
    private Integer mobile;

    @EncryptionField
    private List<User> users;

    @EncryptionField
    private User user;

    public User() {
    }

    public User(String username, Integer password, Integer mobile) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public User(String username, Integer password, Integer mobile, List<User> users) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.users = users;
    }

    public User(String username, Integer password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, Integer password, List<User> users) {
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

    public Integer getPassword() {
        return password;
    }

    public void setPassword(Integer password) {
        this.password = password;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public User(String username, Integer password, Integer mobile, List<User> users, User user) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.users = users;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
