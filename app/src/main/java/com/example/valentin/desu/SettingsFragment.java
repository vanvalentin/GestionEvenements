package com.example.valentin.desu;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    public TextView textViewChangedPwd;
    public TextView textViewChangedInfo;
    public TextView textViewDelete;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        textViewChangedPwd = (TextView)view.findViewById(R.id.textViewChangedPwd);
        textViewChangedInfo = (TextView)view.findViewById(R.id.textViewChangedInfo);
        textViewDelete = (TextView)view.findViewById(R.id.textViewDelete);

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {
        textViewChangedPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mettre un dialog pour changement mdp
            }
        });

        textViewChangedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mettre une activié pour la modif des infos
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mettre un dialog pour la suppression
            }
        });

        super.onActivityCreated(savedInstanceState);
    }
}
