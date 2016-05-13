package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Entity
public class Groupe implements Serializable{

    @Id @GeneratedValue
    private int id;
    private String name;

    @ManyToMany (mappedBy = "groupes")
    private Collection<User> users;

    @ManyToMany(mappedBy = "groupes")
    private Collection<Document> documents;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Groupe() { }

    public Groupe(String name) { this.name = name; }

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

    @JsonIgnore
    public Collection<User> getUsers() {
        return users;
    }
    @JsonSetter
    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    @JsonIgnore
    public Collection<Document> getDocuments() {
        return documents;
    }
    @JsonSetter
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
