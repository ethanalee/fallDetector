package com.example.austin.falldetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    public static Map<String, String> locations = new HashMap<String,String>();
    String timestampA = "";

    public void  Write(String userID, HashMap<String, Double> loc, String email){

        myRef = database.getReference("falls");
        //store in the following format
        //"userId-timestamp": "email-latitude-longitude";
        myRef.child(userID + "-" + System.currentTimeMillis()).setValue(email + "-" + Double.toString(loc.get("latitude")) + Double.toString(loc.get("longitude")));
    }
}
