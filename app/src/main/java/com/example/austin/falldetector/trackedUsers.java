package com.example.austin.falldetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class trackedUsers extends AppCompatActivity {
    public DataManager dataManager = new DataManager();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String userId;
    EditText email;
    EditText lastFell;

    TextView trackedUser;
    String trackedUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_users);

        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPref.getString("USER_ID", "");

        Button register_button = findViewById(R.id.register_button);
        email = (EditText) findViewById(R.id.email_edittext);
        lastFell = (TextView) findViewById(R.id.last_fell);
        trackedUser = (TextView)findViewById(R.id.tracked_user);

        register_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                dataManager.registerUser(userId, email.getText().toString());
            }
        });

        // Read user
        myRef = database.getReference(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String trackedEmail = dataSnapshot.getValue(String.class);
                trackedUserEmail = trackedEmail;
                if(trackedEmail != null){
                    trackedUser.setText(trackedEmail);
                    Log.d("New Tracked User", trackedEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FireBaseError", "Failed to read value.", error.toException());
            }
        });

        // Read falls from database
        myRef = database.getReference("falls");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot snapshotChild: dataSnapshot.getChildren()) {
                    String value = snapshotChild.getValue(String.class);
                    if( value != null ){
                        String[] failRecord = value.split("/");
                        String email = failRecord[0];
                        Double lat = Double.parseDouble(failRecord[1]);
                        Double longitude = Double.parseDouble(failRecord[2]);
                        if(email == trackedUserEmail){
                            String key = dataSnapshot.getKey();
                            String[] keyArray = key.split("-");
                            Long timeStampMillis = Long.parseLong(keyArray[1]);
                            if(System.currentTimeMillis() - timeStampMillis < 3600){
                                lastFell.setText(timeStampMillis.toString());
                            }

                        }
                        //addMarker(lat, longitude);
                    }
                }
                Log.d("Firebase Updated", "yay");

                /*
                String value = dataSnapshot.getValue(String.class);
                if(value != null){
                    Log.d("Firebase Updated", value);
                    //locations.put(Long.toString(System.currentTimeMillis()), value);
                    //timestampA = Long.toString(System.currentTimeMillis());
                }
                */
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FireBaseError", "Failed to read value.", error.toException());
            }
        });

    }
}
