package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TodoDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private Button updateButton;
    private DbHelper dbHelper;
    private int todoId;
    private String title;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(this);
        setContentView(R.layout.activity_todo_details);
        importElements();
        showTodo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.description_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.deleteTodo:
                deleteTodo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importElements() {
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        updateButton = findViewById(R.id.updateButton);
    }

    private void showTodo() {
        Intent intent = getIntent();
        Bundle todo = intent.getExtras();
        title = (String) todo.get("todoTitle");
        description = (String) todo.get("todoDescription");
        todoId = (int) todo.get("todoId");
        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }

    private void deleteTodo() {
        dbHelper.deleteTodo(todoId);
        Intent intent = new Intent(this, TodoActivity.class);
        startActivityForResult(intent, RESULT_OK);
    }

    public void onUpdateClicked(View view) {
        dbHelper.updateTodo(todoId, titleTextView.getText().toString(), descriptionTextView.getText().toString());
    }
}