package com.example.valentin.desu.ClassesFirebase;

import java.sql.Timestamp;

/**
 * Created by Valentin on 25/02/2017.
 */

public class Event {
    public String creatorUID;
    public Timestamp date;
    public String description;
    public String lieu;
    public String nom;
    public boolean prive;

    public Event() {}

    public Event(String creatorUID, Timestamp date, String description, String lieu, String nom, boolean prive) {
        this.creatorUID = creatorUID;
        this.date = date;
        this.description = description;
        this.lieu = lieu;
        this.nom = nom;
        this.prive = prive;
    }
}
