package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

@Entity
public class Role implements Serializable {
    @Id @GeneratedValue
    private int id;
    private String name;
    private int level;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role(){}
    public Role(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public Collection<User> getUsers() {
        return users;
    }
    @JsonSetter
    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
