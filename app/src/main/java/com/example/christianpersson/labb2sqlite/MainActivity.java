package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        userNameEditText = findViewById(R.id.userName);
        passwordEditText = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
    }


    public void onLoginClick(View view) {

        String signInUserName = userNameEditText.getText().toString();
        String signInUserpassword = passwordEditText.getText().toString();

        boolean check = dbHelper.loginUser(signInUserName, signInUserpassword);

        if (!check) {
            Toast.makeText(this, "User doesn´ exist, please register", Toast.LENGTH_SHORT).show();
        } else {
            User user = dbHelper.getCurrenctUser();
            int userId = user.getUserId();
            Intent intent = new Intent(this, TodoActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }

    public void onRegisterClick(View view) {

        String signInUserName = userNameEditText.getText().toString();
        String signInUserpassword = passwordEditText.getText().toString();

        boolean userExists = dbHelper.checkIfuserExists(signInUserName);

        if (!userExists) {
            dbHelper.createUser(signInUserName, signInUserpassword);
            Toast.makeText(this, "User saved to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "User already exists in database", Toast.LENGTH_SHORT).show();
        }


    }
}
