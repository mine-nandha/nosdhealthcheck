package com.mine.nosd.healthcheck.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Roles {
    @Id
    public String name;
    public String role;

    public Roles(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
