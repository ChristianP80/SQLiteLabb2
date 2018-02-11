package com.example.christianpersson.labb2sqlite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    DbHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public static final String LOGGED_IN = "LOGGEDIN";
    public static final String SIGN_IN_USER_NAME = "SIGNINUSERNAME";
    public static final String SIGN_IN_PASSWORD = "SIGNINPASSWORD";
    public static final String IS_LOGGED_IN = "ISLOGGEDIN";
    public static final int SIGNED_IN = 1;
    private int checkIfLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        userNameEditText = findViewById(R.id.userName);
        passwordEditText = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        sharedPreferences = getSharedPreferences(LOGGED_IN, 0);
        checkIfLoggedIn();
    }


    public void onLoginClick(View view) {


        String signInUserName = userNameEditText.getText().toString();
        String signInPassword = passwordEditText.getText().toString();

        List<User> users = dbHelper.getAllUsers();
        for (int i = 0; i <users.size() ; i++) {
            Log.d("chrille", String.valueOf(users.get(i).getUserId()));
        }


        if (signInUserName.isEmpty() || signInPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields", Toast.LENGTH_SHORT).show();
        } else {
            User user = dbHelper.getSpecificUser(signInUserName);
            if (user == null) {
                Toast.makeText(this, "User doesn´t exist", Toast.LENGTH_SHORT).show();
            } else {
                if (signInPassword.equals(user.getUserPassword())) {
                    Intent intent = new Intent(this, TodoActivity.class);
                    int userId = user.getUserId();
                    intent.putExtra("userId", userId);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SIGN_IN_USER_NAME, signInUserName);
                    editor.putString(SIGN_IN_PASSWORD, signInPassword);
                    checkIfLoggedIn = 1;
                    editor.putInt(IS_LOGGED_IN, checkIfLoggedIn);
                    editor.apply();
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(this, "You have entered wrong password", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


/*        String signInUserName = userNameEditText.getText().toString();
        String signInUserpassword = passwordEditText.getText().toString();

        boolean check = dbHelper.loginUser(signInUserName, signInUserpassword);

        if (!check) {
            Toast.makeText(this, "User doesn´ exist, please register", Toast.LENGTH_SHORT).show();
        } else {
            User user = dbHelper.getCurrenctUser();
            int userId = user.getUserId();
            Intent intent = new Intent(this, TodoActivity.class);
            intent.putExtra("userId", userId);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SIGN_IN_USER_NAME, signInUserName);
            editor.putString(SIGN_IN_PASSWORD, signInUserpassword);
            checkIfLoggedIn = 1;
            editor.putInt(IS_LOGGED_IN, checkIfLoggedIn);
            editor.apply();
            startActivity(intent);
        }
    }
*/
    public void checkIfLoggedIn() {
        checkIfLoggedIn = sharedPreferences.getInt(IS_LOGGED_IN, 0);
        if (SIGNED_IN == checkIfLoggedIn) {
            Intent intent = new Intent(this, TodoActivity.class);
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
