package com.example.valentin.desu;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference participantsRef = database.getReference("Participants");
        DatabaseReference eventsRef = database.getReference("Events");

        final List<String> eventsUID = new ArrayList<String>();
        //On recupere la liste des events auquel l'utilisateur est invite
        participantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userUID = mAuth.getCurrentUser().getUid();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.hasChild(userUID)){
                        eventsUID.add(snapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < eventsUID.size(); ++i){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(eventsUID.get(i).equals(snapshot.getKey())){
                            Event currEvent = snapshot.getValue(Event.class);
                            events.add(currEvent.getNom());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,events //changer test par listEvents
        );

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
