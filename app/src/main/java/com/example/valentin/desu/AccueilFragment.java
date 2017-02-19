package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment {

    private Button btnConnexion;
    FragmentActivity context;

    public AccueilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_accueil, container, false);

        btnConnexion = (Button)view.findViewById(R.id.buttonConnexion);
        context = getActivity();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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
                        }else {
                            Toast.makeText(context, "Un des champs texte est vide.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
