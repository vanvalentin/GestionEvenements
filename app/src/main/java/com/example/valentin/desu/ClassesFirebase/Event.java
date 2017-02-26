package com.example.valentin.desu.ClassesFirebase;

import java.sql.Timestamp;

/**
 * Created by Valentin on 25/02/2017.
 */

public class Event {
    private String creatorUID;
    private Timestamp date;
    private String description;
    private String lieu;
    private String nom;
    private boolean prive;

    public Event() {
    }

    public Event(String creatorUID, Timestamp date, String description, String lieu, String nom, boolean prive) {
        this.creatorUID = creatorUID;
        this.date = date;
        this.description = description;
        this.lieu = lieu;
        this.nom = nom;
        this.prive = prive;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLieu() {
        return lieu;
    }

    public String getNom() {
        return nom;
    }

    public boolean isPrive() {
        return prive;
    }
}
