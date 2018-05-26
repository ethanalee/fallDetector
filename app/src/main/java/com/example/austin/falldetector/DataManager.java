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
    DatabaseReference myRef;

    public void DataManager(){
        myRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("FireBase Value", "Value is: " + value);
            }

            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FireBase Reading Error", "Failed to read value.");
            }
        });
    }

    public void  Write(String userID){
        myRef = database.getReference(userID + "-" + System.currentTimeMillis());
        myRef.setValue("Kanata");
    }
}
