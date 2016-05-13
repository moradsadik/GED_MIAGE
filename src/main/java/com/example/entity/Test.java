package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * cree pour tester les relations entre entities.
 * il contient plusieurs class interne (innerClass) dans le meme class Test
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "personne_job", discriminatorType = DiscriminatorType.STRING)
abstract class DemandeClient{
    @Id @GeneratedValue private int id;
    private String reason;
    private String type;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User me;

    // TRANSFER DUN GROUPE A UN AUTRE.
    private int idGroupSource;
    private int idGroupDestination;

    // ID NEW USER POUR LACTIVER.
    private int idUserInscrit;

    // POUR ID DE ROLE QUON VEUT REJOINDRE.
    private int idRoleRequested;

    // POUR ID DE GROUPE QUON VEUT REJOINDRE.
    private int idGroupeRequested;

    //ID DOCUMENT A SUPPRIMER.
    private int idDocument;



}




