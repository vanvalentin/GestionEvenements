package com.example.valentin.desu;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    public ListEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_event, container, false);

        String[] test = {"voila","jambon","kebab"};

        listViewEvents = (ListView)view.findViewById(R.id.List_Events);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,test
        );
        listViewEvents.setAdapter(adapter);

        return view;
    }


}
