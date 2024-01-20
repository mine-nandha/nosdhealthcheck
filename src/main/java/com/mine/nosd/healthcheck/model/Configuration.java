package com.mine.nosd.healthcheck.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
public class Configuration {
    @Id
    public String id;
    public String username;
    public String password;
    public String to;
    public String cc;
    public String firstName;

    public Configuration() {
    }

    public Configuration(String id, String username, String password, String to, String cc, String firstName) {
        this.username = username;
        this.password = password;
        this.to = to;
        this.cc = cc;
        this.firstName = firstName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Configuration [username=" + username + ", password=" + password + ", to=" + to + ", cc=" + cc
                + ", firstName=" + firstName + "]";
    }

}
