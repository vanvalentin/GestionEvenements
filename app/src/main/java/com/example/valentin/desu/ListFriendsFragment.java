package com.example.valentin.desu;

import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.valentin.desu.ClassesFirebase.Event;
import com.example.valentin.desu.ClassesFirebase.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListFriendsFragment extends Fragment {

    public FloatingActionButton fab;

    public LinkedList<String> listFriends =  new LinkedList<String>();
    public LinkedList<String> listUsers;
    public HashMap<String, String> mapUsers;

    public ListView listViewFriends;
    public ArrayAdapter<String> adapter;
    public ArrayAdapter<String> adapterseach; // pour la recherche
    FragmentActivity context;
    private FirebaseAuth mAuth;

    private static final String TAG = ListFriendsFragment.class.getSimpleName();

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
        listUsers = new LinkedList<String>();
        mapUsers = new HashMap<String, String>();

        context = getActivity();
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityCreated(Bundle savedInstanceState) {

        //On recupere la liste d'ami
        final List<String> friendsIDs = new LinkedList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference friendsOfUserRef = database.getReference("Friends").child(mAuth.getCurrentUser().getUid());

        friendsOfUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //get info de l'ami (on a uid)
                    friendsIDs.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        DatabaseReference usersRef = database.getReference("Users");
        final List<User> friendsInfos = new LinkedList<User>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(String id : friendsIDs){
                        if(snapshot.getKey().equals(id)){
                            friendsInfos.add(snapshot.getValue(User.class));
                        }
                    }
                }
                for(User u : friendsInfos){
                    adapter.add(u.Prenom + " " + u.Nom);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });


        //On gere l'ajout d'amis
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = context.getLayoutInflater().inflate(R.layout.dialog_search_friends,null);
                final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)mView.findViewById(R.id.autoCompleteTextView);
                Button buttonValide = (Button)mView.findViewById(R.id.buttonValide);


                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userUID = mAuth.getCurrentUser().getUid();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(!snapshot.getKey().equals(userUID)){
                                User currentUser = snapshot.getValue(User.class);
                                mapUsers.put(snapshot.getKey(), currentUser.Prenom + " " + currentUser.Nom);
                                listUsers.add(currentUser.Prenom + " " + currentUser.Nom);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                autoCompleteTextView.setText("");
                adapterseach = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, listUsers );
                autoCompleteTextView.setAdapter(adapterseach);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                buttonValide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userToAdd = autoCompleteTextView.getText().toString();
                        String friendID = "";
                        for (Map.Entry<String, String> e : mapUsers.entrySet()) {
                            if(e.getValue().equals(userToAdd)){
                                friendID = e.getKey();
                            }
                        }
                        if(!friendID.isEmpty()) {

                            final boolean[] friendAdded = new boolean[1];
                            final String tmpFriendID = friendID;

                            final List<String> friendsIDs = new LinkedList<String>();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference friendsOfUserRef = database.getReference("Friends").child(mAuth.getCurrentUser().getUid());

                            friendsOfUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        //get info de l'ami (on a uid)
                                        friendsIDs.add(snapshot.getKey());
                                    }
                                    friendAdded[0] = addFriend(mAuth.getCurrentUser().getUid(), tmpFriendID, friendsIDs);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                }
                            });


                            if (friendAdded[0]){
                                adapter.add(autoCompleteTextView.getText().toString());
                                dialog.cancel();
                            }else{
                                Toast.makeText(context, "Cet utilisateur est déjà votre ami.", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(context, "Aucun utilisateur trouvé.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean addFriend(String pUserID, final String pFriendID, List<String> pFriends){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        if(pFriends.contains(pFriendID))
            return false;

        DatabaseReference friendsRef = database.getReference().child("Friends");
        DatabaseReference newUserFriendsRef = friendsRef.child(pUserID);

        Map<String, Object> friendData = new HashMap<String, Object>();
        friendData.put(pFriendID, true);
        newUserFriendsRef.setValue(friendData);

        return true;
    }

}
