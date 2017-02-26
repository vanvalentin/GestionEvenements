package com.example.valentin.desu.ClassesFirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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


    /*public static User getUserFromUID(DatabaseReference dbRef, final String uid){
        final User[] resultUser = new User[1];
        DatabaseReference lUsersRef = FirebaseDatabase.getInstance().getReference("Users");
        lUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equals(uid)){
                        resultUser[0] = snapshot.getValue(User.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        return resultUser[0];
    }*/
}
