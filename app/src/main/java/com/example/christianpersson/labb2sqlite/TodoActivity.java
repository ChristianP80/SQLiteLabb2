package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private User currentUser = new User();
    private ListView todoListView;
    private EditText titelEditText;
    private EditText contentEditText;
    private Button addButton;
    private List<Todo> todoList;
    private List<String> titleList;
    private Spinner categorySpinner;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        importElements();
        dbHelper = new DbHelper(this);

        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        if (b != null){
            int userID = b.getInt("userId");
            currentUser.setUserId(userID);
        }
        Toast.makeText(this, "Signed in with userId: " + currentUser.getUserId(), Toast.LENGTH_SHORT).show();


        todoList = dbHelper.getAllTodos(currentUser.getUserId());
        titleList = new ArrayList<>();
        for (int i = 0; i < todoList.size(); i++) {
            titleList.add(todoList.get(i).getTodoTitle());
        }


        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titleList);

        todoListView.setAdapter(adapter);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getApplicationContext(), TodoDetailsActivity.class);
                String title = todoList.get(position).getTodoTitle();
                String description = todoList.get(position).getTodoContent();
                myIntent.putExtra("todoTitle", title);
                myIntent.putExtra("todoDescription", description);
                startActivity(myIntent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String title = titelEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String categories = categorySpinner.getSelectedItem().toString();
                int category;
                if (categories.equals("arbete")) {
                    category = 0;
                } else if (categories.equals("fritid")) {
                    category = 1;
                } else if (categories.equals("viktigt")) {
                    category = 2;
                } else {
                    category = 0;
                }
                int userID = currentUser.getUserId();
                dbHelper.createTodo(title, content, category, userID);
                titleList.add(titelEditText.toString());
                Todo todo = new Todo(title, content, category, userID);
                todoList.add(todo);
                titelEditText.setVisibility(View.INVISIBLE);
                contentEditText.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.INVISIBLE);

                todoListView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void importElements() {
        todoListView = findViewById(R.id.todoListView);
        titelEditText = findViewById(R.id.TitleEditText);
        contentEditText = findViewById(R.id.descriptionEditText);
        addButton = findViewById(R.id.addButton);
        categorySpinner = findViewById(R.id.categorySpinner);

        titelEditText.setVisibility(View.INVISIBLE);
        contentEditText.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        categorySpinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTodoButton:
                titelEditText.setVisibility(View.VISIBLE);
                contentEditText.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);
                todoListView.setVisibility(View.INVISIBLE);
                return true;
            case R.id.statistics:
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
