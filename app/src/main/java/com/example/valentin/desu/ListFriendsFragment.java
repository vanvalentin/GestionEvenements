package com.example.valentin.desu;

import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;

public class ListFriendsFragment extends Fragment {

    public FloatingActionButton fab;

    public LinkedList<String> listFriends =  new LinkedList<String>();
    public String[] listFriendsTest = {"test", "ok"}; // faire une liste pour la base de données
    public ListView listViewFriends;
    public ArrayAdapter<String> adapter;
    public ArrayAdapter<String> adapterseach; // pour la recherche
    FragmentActivity context;



    public ListFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_friends, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listViewFriends = (ListView)view.findViewById(R.id.listFriends);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listFriends);
        listViewFriends.setAdapter(adapter);

        context = getActivity();
        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = context.getLayoutInflater().inflate(R.layout.dialog_search_friends,null);
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)mView.findViewById(R.id.autoCompleteTextView);
                Button buttonValide = (Button)mView.findViewById(R.id.buttonValide);

                autoCompleteTextView.setText("");
                adapterseach = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listFriendsTest);
                autoCompleteTextView.setAdapter(adapterseach);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                buttonValide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.add(autoCompleteTextView.getText().toString());
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

}
