package com.bnyte.encryption.vo;

import com.bnyte.encryption.bind.EncryptionField;

import java.util.List;

/**
 * @author bnyte
 * @date 2022/4/15 1:04
 * @mail bnytezz@gmail.com
 */
public class User {

    public static User getInstance() {
        return new User("ggboy_username", "ggboy_password", List.of(
                new User("ggboy1_inner_username", "ggboy1_inner_username"),
                new User("ggboy1_inner_username", "ggboy1_inner_username", List.of(
                        new User("ggboy1_inner_username", "ggboy1_inner_username"),
                        new User("ggboy1_inner_username", "ggboy1_inner_username")
                ))
        ));
    }

    private String username;

    @EncryptionField // 属性有这个注解的话就会被加密
    private String password;

    private List<User> users;

    public User() {
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
}
