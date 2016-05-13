package com.example.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Tag implements Serializable {

    @Id @GeneratedValue
    private int id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    public Tag() { }

    public Tag(String name) { this.name = name; }

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
    public Document getDocument() {
        return document;
    }
    @JsonSetter
    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
