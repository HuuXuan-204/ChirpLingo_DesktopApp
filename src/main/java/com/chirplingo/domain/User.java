package com.chirplingo.domain;

<<<<<<< HEAD
import com.chirplingo.domain.base.BaseEntity;

public class User extends BaseEntity {
    private String email;
    private String userName;
    private String avatar;

    public User(String email, String userName, String avatar) {
        this.email = email;
        this.userName = userName;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setEmail(String email) {
        this.email = email;
        triggerUpdate();
    }

    public void setUserName(String userName) {
        this.userName = userName;
        triggerUpdate();

    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        triggerUpdate();
    }
=======
public class User {
>>>>>>> main
}
