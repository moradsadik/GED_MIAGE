package com.example.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public class Categorie implements Serializable {

    @Id @GeneratedValue
    private int id;
    private String name;

    @OneToMany(mappedBy = "categorie")
    private List<Document> documents;

    public Categorie() { }

    public Categorie(String name) { this.name = name; }

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
    public List<Document> getDocuments() {
        return documents;
    }
    @JsonSetter
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
