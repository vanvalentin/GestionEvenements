package com.example.valentin.desu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.valentin.desu.ClassesFirebase.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListEventsFragment extends Fragment {

    public LinkedList<String> listEvents =  new LinkedList<String>();
    public ListView listViewEvents;
    public SearchView searchViewEvents;
    public ArrayAdapter<String> adapter;
    public List<String> events;
    private FirebaseAuth mAuth;

    public ListEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_events, container, false);



        listViewEvents = (ListView)view.findViewById(R.id.List_Events);
        events = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,events //changer test par listEvents
        );
        listViewEvents.setAdapter(adapter);



        searchViewEvents = (SearchView)view.findViewById(R.id.SearchView_Events);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                EventFragment eventFragment = new EventFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("nom", adapter.getItem(position));
                eventFragment.setArguments(args);
                ft.replace(R.id.content_main, eventFragment, eventFragment.getTag()).commit();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference participantsRef = database.getReference("Participants");

        final List<String> eventsID = new ArrayList<String>();
        ValueEventListener participantListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userUID = mAuth.getCurrentUser().getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot participants : snapshot.getChildren()) {
                        if(participants.getKey().equals(userUID)) {
                            eventsID.add(snapshot.getKey());
                            continue;
                        }
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference eventsRef = database.getReference("Events");

                eventsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for(String e : eventsID) {
                                if (snapshot.getKey().equals(e)){
                                    DataSnapshot currEventName = snapshot.child("nom");
                                    adapter.add(currEventName.getValue(String.class));
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        participantsRef.addValueEventListener(participantListener);


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
