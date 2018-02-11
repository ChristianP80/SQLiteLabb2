package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    private TextView descriptionText;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        descriptionText = findViewById(R.id.descriptionTextView);
        getIntentExtra();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        Bundle user = intent.getExtras();
        userId = (int)user.get("userID");
    }
}
