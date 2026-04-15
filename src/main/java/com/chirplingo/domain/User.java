package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import java.time.OffsetDateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User extends BaseEntity {
    private StringProperty email;
    private StringProperty userName;
    private StringProperty avatar;

    public User() {
        super();
        this.email = new SimpleStringProperty();
        this.userName = new SimpleStringProperty();
        this.avatar = new SimpleStringProperty();
    }

    public User(String id, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean isSynced, 
                String email, String userName, String avatar) {
        super(id, createdAt, updatedAt, isSynced);
        this.email = new SimpleStringProperty(email);
        this.userName = new SimpleStringProperty(userName);
        this.avatar = new SimpleStringProperty(avatar);
    }

    public String getEmail() {
        return email.get(); 
    }

    public void setEmail(String email) {
        this.email.set(email); 
        triggerUpdate();       
    }

    public StringProperty emailProperty() {
        return email; 
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
        triggerUpdate();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public String getAvatar() {
        return avatar.get();
    }

    public void setAvatar(String avatar) {
        this.avatar.set(avatar);
        triggerUpdate();
    }

    public StringProperty avatarProperty() {
        return avatar;
    }
}