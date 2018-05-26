package com.example.austin.falldetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class trackedUsers extends AppCompatActivity {
    public DataManager dataManager = new DataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_users);

        SharedPreferences sharedPref = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("USER_ID", "");
        dataManager.registerUser(userId, "fakeRecord");

    }
}
