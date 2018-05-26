package com.example.austin.falldetector;

import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("start");
    public static Map<String, String> locations = new HashMap<String,String>();
    String timestampA = "";

    public void  Write(String userID, HashMap<String, Double> loc){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if(value != null){
                    locations.put(Long.toString(System.currentTimeMillis()), value);
                    timestampA = Long.toString(System.currentTimeMillis());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FireBaseError", "Failed to read value.", error.toException());
            }
        });

        myRef = database.getReference(userID + "-" + System.currentTimeMillis());
        if(locations.get(timestampA)!=null)
            Log.w("FireBaseMap", locations.get(timestampA));
        //Log.d("MyLocation", Double.toString(loc.get("latitude")));
        myRef.child("latitude").setValue(loc.get("latitude"));
        myRef.child("longitude").setValue(loc.get("longitude"));

    }
}
