package com.example.valentin.desu.ClassesFirebase;

/**
 * Created by Valentin on 20/02/2017.
 */

public class User {
    public String Nom;
    public String Prenom;
    public String Tel;
    public String Email;

    public User(){}

    public User(String pNom, String pPrenom, String pTel, String pEmail){
        this.Nom = pNom;
        this.Prenom = pPrenom;
        this.Tel = pTel;
        this.Email = pEmail;
    }
}
