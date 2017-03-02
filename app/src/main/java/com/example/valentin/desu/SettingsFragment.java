package com.example.valentin.desu;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    public TextView textViewChangedPwd;
    public TextView textViewChangedInfo;
    public TextView textViewDelete;
    FragmentActivity context;
    private FirebaseAuth mAuth;
    private AlertDialog dialogChangeMdp;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        textViewChangedPwd = (TextView)view.findViewById(R.id.textViewChangedPwd);
        context = getActivity();
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {
        textViewChangedPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View mView = context.getLayoutInflater().inflate(R.layout.dialog_login, null);
                final EditText mPwd = (EditText) mView.findViewById(R.id.editText_ancienmdp);
                final EditText mNewPwd = (EditText) mView.findViewById(R.id.editText_newmdp);
                final EditText mConfirmNewPwd = (EditText) mView.findViewById(R.id.editText_confirmnewmdp);
                Button mBtnChanger = (Button) mView.findViewById(R.id.btn_changemdp);

                mBtnChanger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mPwd.getText().toString().isEmpty() && !mNewPwd.getText().toString().isEmpty() && mNewPwd.getText().toString().equals(mConfirmNewPwd.getText().toString())){
                            //Essayer de se connecter TODO
                            String password = mPwd.getText().toString();
                            String newpwd = mNewPwd.getText().toString();

                            FirebaseUser user = mAuth.getCurrentUser();


                            user.updatePassword(newpwd)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(context, "Echec du changement de mot de passe.",
                                                        Toast.LENGTH_SHORT).show();
                                            }else{
                                                dialogChangeMdp.dismiss();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "Un des champs texte est vide.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setView(mView);
                dialogChangeMdp = builder.create();
                dialogChangeMdp.show();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }
}
