package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private TextView descriptionText;
    private TextView countTextView;
    private Spinner categorySpinner;
    private int userId;
    private String category;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        descriptionText = findViewById(R.id.description);
        categorySpinner = findViewById(R.id.categorySpinner);
        countTextView = findViewById(R.id.countTextView);
        dbHelper = new DbHelper(this);
        category = "Fritid";
        setSpinnerAdapter();
        getIntentExtra();
        calculateNumberOfTodos();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        Bundle user = intent.getExtras();
        userId = (int)user.get("userID");
    }
    private void calculateNumberOfTodos() {
        descriptionText.setText("Number of todos in " + category);
        List<Todo> todos;
        Log.d("chrille", "UserId: " + userId + "Category: " + category);
        todos = dbHelper.getAllTodosInCategory(userId, category);
//        int numberOfTodos = todos.size();
//        countTextView.setText(numberOfTodos);
    }
    private void setSpinnerAdapter() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    category = "Fritid";
                    calculateNumberOfTodos();
                } else if (position == 1){
                    category = "Arbete";
                    calculateNumberOfTodos();
                } else if (position == 2) {
                    category = "Viktigt";
                    calculateNumberOfTodos();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
