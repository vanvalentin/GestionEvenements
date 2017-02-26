package com.example.valentin.desu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.valentin.desu.ClassesFirebase.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private String nomEvent;
    public ArrayAdapter<String> adapterParticipants;
    private List<String> participants;
    private ListView listViewParticipants;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        nomEvent = getArguments().getString("nom");

        listViewParticipants = (ListView)view.findViewById(R.id.List_Participants);
        participants = new LinkedList<String>();
        adapterParticipants = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,participants //changer test par listEvents
        );
        listViewParticipants.setAdapter(adapterParticipants);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("Events");

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uidEvent = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot currEventName = snapshot.child("nom");
                    if(currEventName.getValue(String.class).equals(nomEvent)){
                        uidEvent = snapshot.getKey();
                    }
                }

                if(!uidEvent.isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference participantsRef = database.getReference("Participants");

                    final String finalUidEvent = uidEvent;
                    participantsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<String> participantsUID = new LinkedList<String>();
                            for (DataSnapshot snapshotParticipants : dataSnapshot.getChildren()) {
                                if(snapshotParticipants.getKey().equals(finalUidEvent)) {
                                    for (DataSnapshot part : snapshotParticipants.getChildren()) {
                                        if(part.getValue(boolean.class)){
                                            participantsUID.add(part.getKey());
                                        }
                                    }
                                }
                            }

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference usersRef = database.getReference("Users");

                            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshotUser : dataSnapshot.getChildren()) {
                                        for(String p : participantsUID) {
                                            if (snapshotUser.getKey().equals(p)) {
                                                User currUser = snapshotUser.getValue(User.class);
                                                adapterParticipants.add(currUser.Prenom + " " + currUser.Nom);
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
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onActivityCreated(savedInstanceState);
    }


}
