package com.example.austin.falldetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class confirmPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);

        Button button = findViewById(R.id.getUpButton);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                getUp();
            }
        });
    }

    public void getUp(){
        Intent i;
        i = new Intent(this, trackedUsers.class);
        startActivity(i);
    }
}
