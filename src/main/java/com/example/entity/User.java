package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class User implements Serializable {

    @Id @GeneratedValue
    private int id;
    private String name;
    private String prenom;
    private String mail;
    private String password;
    private Boolean active;

    @ManyToMany
    @JoinTable( name="USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany
    @JoinTable( name="USER_GROUP",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Groupe> groupes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Document> documents;


    public User() { }
    public User(String name, String prenom, String mail) {
        this.name = name;
        this.prenom = prenom;
        this.mail = mail;
        this.active=false;
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

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Set<Groupe> getGroupes() {
        return groupes;
    }
    public void setGroupes(Set<Groupe> groupes) {
        this.groupes = groupes;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public Collection<Document> getDocuments() {
        return documents;
    }
    @JsonSetter
    public void setDocuments(Collection<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prenom='" + prenom + '\'' +
                ", e-mail=" + mail +
                '}';
    }


}
