package com.example.austin.falldetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_users);

        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPref.getString("USER_ID", "");

        Button register_button = findViewById(R.id.register_button);
        email = (EditText) findViewById(R.id.email_edittext);

        register_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                dataManager.registerUser(userId, email.getText().toString());
            }
        });

        // Read from the database
        myRef = database.getReference(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Boolean lol = true;

                for (DataSnapshot snapshotChild: dataSnapshot.getChildren()) {
                    String value = snapshotChild.getValue(String.class);
                    if( value != null ){

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
