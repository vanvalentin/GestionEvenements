package com.example.valentin.desu;


import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.valentin.desu.ClassesFirebase.Event;
import com.example.valentin.desu.ClassesFirebase.EventMessage;
import com.example.valentin.desu.ClassesFirebase.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private String nomEvent;
    public ArrayAdapter<String> adapterParticipants;
    private List<String> participants;
    private ListView listViewParticipants;

    public ArrayAdapter<String> adapterMessages;
    private List<String> messages;
    private ListView listViewMessages;
    private ImageView buttonSend;
    private EditText editTextMsg;
    private FirebaseAuth mAuth;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        nomEvent = getArguments().getString("nom");

        buttonSend = (ImageView)view.findViewById(R.id.image_SendComment);
        editTextMsg = (EditText)view.findViewById(R.id.editText_Msg);

        listViewParticipants = (ListView)view.findViewById(R.id.List_Participants);
        participants = new LinkedList<String>();
        adapterParticipants = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,participants //changer test par listEvents
        );
        listViewParticipants.setAdapter(adapterParticipants);

        listViewMessages = (ListView)view.findViewById(R.id.List_Message);
        messages = new LinkedList<String>();
        adapterMessages = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,messages //changer test par listEvents
        );
        listViewMessages.setAdapter(adapterMessages);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        listViewParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ProfilFragment profilFragment = new ProfilFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("nom", adapterParticipants.getItem(position));
                profilFragment.setArguments(args);
                ft.replace(R.id.content_main, profilFragment, profilFragment.getTag()).commit();
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventsRef = database.getReference("Events");

        //Affichage des participants
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

        //Ajout d'un message
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference eventsRef = database.getReference("Events");
                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String msg = editTextMsg.getText().toString();
                        String uidEvent = "";
                        String userUID = mAuth.getCurrentUser().getUid();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            DataSnapshot currEventName = snapshot.child("nom");
                            if (currEventName.getValue(String.class).equals(nomEvent)) {
                                uidEvent = snapshot.getKey();
                            }
                        }
                        if(!uidEvent.isEmpty())
                            addMessage(userUID, uidEvent, msg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //Affichage des messages
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uidEvent = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot currEventName = snapshot.child("nom");
                    if (currEventName.getValue(String.class).equals(nomEvent)) {
                        uidEvent = snapshot.getKey();
                    }
                }

                if (!uidEvent.isEmpty()) {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference participantsRef = database.getReference("EventMessages");

                    final String finalUidEvent = uidEvent;
                    participantsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<String> participantsUID = new LinkedList<String>();
                            for (DataSnapshot snapshotEvents : dataSnapshot.getChildren()) {
                                if (snapshotEvents.getKey().equals(finalUidEvent)) {
                                    for (final DataSnapshot message : snapshotEvents.getChildren()) {

                                        DatabaseReference usersRef = database.getReference("Users");
                                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshotUser : dataSnapshot.getChildren()) {
                                                    EventMessage msg = message.getValue(EventMessage.class);
                                                    if (snapshotUser.getKey().equals(msg.Poster)) {
                                                        User currUser = snapshotUser.getValue(User.class);

                                                        messages.add(0, msg.Message + "\n\n- " + currUser.Prenom + " " + currUser.Nom);
                                                        adapterMessages.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }
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

    @TargetApi(Build.VERSION_CODES.N)
    public void addMessage(String pCreatorID, String pEventID, String pMessage){
        if(!pMessage.isEmpty()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference eventMsgRef = database.getReference().child("EventMessages").child(pEventID);
            DatabaseReference newMsgRef = eventMsgRef.push();

            EventMessage eventMsg = new EventMessage(pMessage, pCreatorID);

            newMsgRef.setValue(eventMsg);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
