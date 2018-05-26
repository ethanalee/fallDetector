package com.example.austin.falldetector;

import android.content.Context;
import android.content.Intent;
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
    DatabaseReference fallsRef;

    fallDetector detector;

    String userId;
    EditText email;

    TextView trackedUser;
    String trackedUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_users);
        detector = new fallDetector(this) {};

        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPref.getString("USER_ID", "");

        Button register_button = findViewById(R.id.register_button);
        email = (EditText) findViewById(R.id.email_edittext);
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
        fallsRef = database.getReference("falls");
        fallsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot snapshotChild: dataSnapshot.getChildren()) {
                    String value = snapshotChild.getValue(String.class);
                    if( value != null ){
                        String[] fallRecord = value.split("/");
                        String email = fallRecord[0];
                        Log.d("New Fall", email);
                        Log.d("New Fall Email", trackedUserEmail);
                        Double fallTime = Double.parseDouble(fallRecord[3]);
                        //Double timeDiff = System.currentTimeMillis() - fallTime;
                        if(email.equals(trackedUserEmail) && (System.currentTimeMillis() - fallTime < 30000 )){
                            goToMaps(fallRecord[1], fallRecord[2]);
                            /*
                            String key = dataSnapshot.getKey();
                            String[] keyArray = key.split("-");
                            Long timeStampMillis = Long.parseLong(keyArray[1]);
                            if(System.currentTimeMillis() - timeStampMillis < 3600){
                                Log.d("Your User Fell Recently", email);
                                goToMaps(fallRecord[1], fallRecord[2]);
                            }
                            */
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
    public void goToMaps(String lat, String longitude){
        Intent mapsIntent;
        mapsIntent = new Intent(this, MapsActivity.class);
        mapsIntent.putExtra("latitude", lat);
        mapsIntent.putExtra("longitude", longitude);
        startActivity(mapsIntent);
    }
}
