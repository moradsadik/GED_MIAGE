package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Collection;
import java.util.Date;
import java.util.Set;


@Entity
public class Document implements Serializable{

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private String type;
    private long size;
    private double version;
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Basic(fetch=FetchType.LAZY)
    @Lob
    private byte[] file;

    @OneToMany(orphanRemoval=true,mappedBy = "document")
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToMany
    @JoinTable( name="DOCUMENT_GROUP",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Groupe> groupes;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    public Document() { }

    public Document(String name, String description, String type, long size, byte[] file, String path) {
        this.name = name;
        this.path= path;
        this.description = description;
        this.type = type;
        this.size = size;
        this.file = file;
        this.date = new Date();
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getFile() {
        return file;
    }
    public void setFile(byte[] file) {
        this.file = file;
    }

    public Set<Tag> getCles() {
        return tags;
    }
    public void setCles(Set<Tag> cles) {
        this.tags = cles;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Set<Groupe> getGroupes() {
        return groupes;
    }
    public void setGroupes(Set<Groupe> groupes) {
            this.groupes = groupes;
    }

    public Categorie getCategorie() {
        return categorie;
    }
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }


    @JsonIgnore
    public String getPath() {
        return path;
    }
    @JsonSetter
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Document{" +
                "size=" + size +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", path=" + path +
                '}';
    }

}
