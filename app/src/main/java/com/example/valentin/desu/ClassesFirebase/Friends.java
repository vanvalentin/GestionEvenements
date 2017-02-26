package com.example.valentin.desu.ClassesFirebase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentin on 25/02/2017.
 */

public class Friends {
    public List<String> friendsUID;

    public Friends() {
        friendsUID = new LinkedList<String>();
    }

    public Friends(List<String> friendsUID) {
        this.friendsUID = friendsUID;
    }
}
