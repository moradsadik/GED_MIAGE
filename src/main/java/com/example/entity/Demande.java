package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "demande_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Demande implements Serializable{
    @Id @GeneratedValue
    private int id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;



}
