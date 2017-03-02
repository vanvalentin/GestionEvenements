package com.example.valentin.desu;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.valentin.desu.ClassesFirebase.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    private String nomUser;
    private TextView textViewNom;
    private TextView textViewTel;
    private TextView textViewEmail;

    private static final String TAG = ListFriendsFragment.class.getSimpleName();
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    public ProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        nomUser = getArguments().getString("nom");
        textViewNom = (TextView)view.findViewById(R.id.textView_NameProfil);
        textViewTel = (TextView)view.findViewById(R.id.textView_phoneProfil);
        textViewEmail = (TextView)view.findViewById(R.id.textView_EmailProfil);

        return view;
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User currUser = snapshot.getValue(User.class);
                    String name = currUser.Prenom + " " + currUser.Nom;
                    if(name.equals(nomUser)){
                        textViewNom.setText(name);
                        textViewTel.setText(currUser.Tel);
                        textViewEmail.setText(currUser.Email);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onActivityCreated(savedInstanceState);
    }

}
