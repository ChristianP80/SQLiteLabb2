package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private User currentUser;
    private ListView todoListView;
    private EditText titleEditText;
    private EditText contentEditText;
    private Button addButton;
    private List<Todo> todoList;
    private List<String> titleList;
    private Spinner categorySpinner;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    private int userID;
    public static final String LIST_LOADED = "LISTLOADED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        currentUser = new User();
        getIntentExtras();
 //       sharedPreferences = getSharedPreferences(LIST_LOADED, 0);
 //       setPrefs();
        importElements();
        dbHelper = new DbHelper(this);
        setTitleandTodoList();
        setAdapter();
        for (int i = 0; i <titleList.size() ; i++) {
            Log.d("chrille ", titleList.get(i));
            Log.d("chrille ", "categoryID" + String.valueOf(todoList.get(i).getTodoCategoryId()));
        }










        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String categories = categorySpinner.getSelectedItem().toString();
                int category;
                if (categories.equals("arbete")) {
                    category = 1;
                } else if (categories.equals("fritid")) {
                    category = 2;
                } else if (categories.equals("viktigt")) {
                    category = 3;
                } else {
                    category = 0;
                }
                int userID = currentUser.getUserId();
                dbHelper.createTodo(title, content, category, userID);
                titleList.add(titleEditText.getText().toString());
                Todo todo = new Todo(title, content, category, userID);
                todoList.add(todo);
                titleEditText.setVisibility(View.INVISIBLE);
                contentEditText.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                categorySpinner.setVisibility(View.INVISIBLE);

                todoListView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                titleEditText.setText("");
                contentEditText.setText("");

            }
        });
    }

//    private void setPrefs() {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("USERID", userID);
//        editor.apply();
//    }

    private void setAdapter() {
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, titleList);

        todoListView.setAdapter(adapter);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(getApplicationContext(), TodoDetailsActivity.class);
                String title = todoList.get(position).getTodoTitle();
                String description = todoList.get(position).getTodoContent();
                int todoId = todoList.get(position).getTodoId();
                detailsIntent.putExtra("todoId", todoId);
                detailsIntent.putExtra("todoTitle", title);
                detailsIntent.putExtra("todoDescription", description);
                startActivity(detailsIntent);
            }
        });
    }

    private void setTitleandTodoList() {
        todoList = dbHelper.getAllTodos(currentUser.getUserId());
//        for (int i = 0; i <todoList.size() ; i++) {
//            Log.d("chrille", String.valueOf(todoList.get(i).getTodoId()));
//        }

        titleList = new ArrayList<>();
        for (int i = 0; i < todoList.size(); i++) {
            titleList.add(todoList.get(i).getTodoTitle());
        }
    }

    private void getIntentExtras() {

        Intent myIntent = getIntent();
        Bundle b = myIntent.getExtras();
        if (b != null){
            userID = b.getInt("userId");
            currentUser.setUserId(userID);
        }
        Toast.makeText(this, "Signed in with userId: " + currentUser.getUserId(), Toast.LENGTH_SHORT).show();
    }

    private void importElements() {
        todoListView = findViewById(R.id.todoListView);
        titleEditText = findViewById(R.id.TitleEditText);
        contentEditText = findViewById(R.id.descriptionEditText);
        addButton = findViewById(R.id.addButton);
        categorySpinner = findViewById(R.id.categorySpinner);

        titleEditText.setVisibility(View.INVISIBLE);
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
                titleEditText.setVisibility(View.VISIBLE);
                contentEditText.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                categorySpinner.setVisibility(View.VISIBLE);
                todoListView.setVisibility(View.INVISIBLE);
                return true;
            case R.id.statistics:
                Intent statsIntent = new Intent(this, StatsActivity.class);
                statsIntent.putExtra("userID", currentUser.getUserId());
                startActivity(statsIntent);
                return true;
            case R.id.signOut:
                Intent intent = new Intent(this, MainActivity.class);
                sharedPreferences = getSharedPreferences(MainActivity.LOGGED_IN, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(MainActivity.IS_LOGGED_IN, 0);
                editor.apply();
                startActivity(intent);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
