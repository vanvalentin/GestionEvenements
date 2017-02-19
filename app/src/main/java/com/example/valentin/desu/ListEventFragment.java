package com.example.valentin.desu;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;

public class ListEventFragment extends Fragment {

    public LinkedList<String> listEvents =  new LinkedList<String>();
    public ListView listViewEvents;
    public SearchView searchViewEvents;
    public ArrayAdapter<String> adapter;
    public String[] test = {"voila","jambon","kebab"};


    public ListEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_event, container, false);



        listViewEvents = (ListView)view.findViewById(R.id.List_Events);
        adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,test //changer test par listEvents
        );
        listViewEvents.setAdapter(adapter);

        searchViewEvents = (SearchView)view.findViewById(R.id.SearchView_Events);

        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {
        searchViewEvents.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

}
