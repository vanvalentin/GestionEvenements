package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.valentin.desu.ClassesFirebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment {

    private Button btnConnexion;
    private Button btnSignup;
    FragmentActivity context;
    Toolbar toolbar;
    private AlertDialog dialogLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AccueilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_accueil, container, false);

        btnConnexion = (Button)view.findViewById(R.id.buttonConnexion);
        btnSignup = (Button)view.findViewById(R.id.buttonInscription);
        context = getActivity();
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        ((DrawerLocker) context).setDrawerEnabled(false);


        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View mView = context.getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mEmail = (EditText) mView.findViewById(R.id.editText_emailLogin);
                final EditText mPassword = (EditText) mView.findViewById(R.id.editText_passwordLogin);
                Button mBtnLogin = (Button) mView.findViewById(R.id.btn_connexionLogin);

                mBtnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mEmail.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()){
                            //Essayer de se connecter TODO
                            String email = mEmail.getText().toString();
                            String password = mPassword.getText().toString();

                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(context, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                dialogLogin.dismiss();
                                            }

                                            // ...
                                        }
                                    });
                            ((DrawerLocker) context).setDrawerEnabled(true);
                        }else {
                            Toast.makeText(context, "Un des champs texte est vide.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setView(mView);
                dialogLogin = builder.create();
                dialogLogin.show();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mNom = ((EditText) context.findViewById(R.id.editText_Nom)).getText().toString();
                final String mPrenom = ((EditText) context.findViewById(R.id.editText_Prenom)).getText().toString();
                final String mTel = ((EditText) context.findViewById(R.id.editText_TelMobile)).getText().toString();
                final String mMail = ((EditText) context.findViewById(R.id.editText_mail)).getText().toString();
                final String mPassword = ((EditText) context.findViewById(R.id.editText_pwd)).getText().toString();
                final String mPasswordConfirmation = ((EditText) context.findViewById(R.id.editText_confirmPwd)).getText().toString();

                if(!mNom.isEmpty() && !mPrenom.isEmpty() && !mTel.isEmpty() && !mMail.isEmpty() &&
                        !mPassword.isEmpty() && !mPasswordConfirmation.isEmpty()) {

                    if(mPasswordConfirmation.equals(mPassword)){

                        createAccount(new User(mNom, mPrenom, mTel, mMail), mPassword);

                    }else{
                        Toast.makeText(context, "La confirmation du mot de passe a échoué.",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Un des champs est vide.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        super.onActivityCreated(savedInstanceState);
    }

    private void createAccount(final User pUser, String pPassword) {
        mAuth.createUserWithEmailAndPassword(pUser.Email, pPassword)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersRef = database.getReference().child("Users");

                            usersRef = usersRef.child(user.getUid());
                            usersRef.setValue(pUser);

                        }
                    }
                });
    }
}
