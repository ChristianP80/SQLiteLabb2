package com.example.christianpersson.labb2sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christianpersson on 2018-02-05.
 */

public class DbHelper extends SQLiteOpenHelper {


    private User currentUser = new User();
    private static final String DATABASE_NAME = "TBD.db";

    private static final int DATABASE_VERSION = 1;

    //USER Table
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "userID";
    private static final String COLUMN_USER_USER = "user";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_USER_USER + " TEXT, " +
                    COLUMN_USER_PASSWORD + " TEXT " +
                    ")";

    // CATEGORY Table
    private static final String TABLE_CATEGORY = "Category";
    private static final String COLUMN_CATEGORY_ID = "categoryID";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";
    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + TABLE_CATEGORY + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY , " +
                    COLUMN_CATEGORY_NAME + " TEXT " +
                    ")";

    // TODOLIST TABLE
    private static final String TABLE_TODO = "Todo";
    private static final String COLUMN_TODO_ID = "todoID";
    private static final String COLUMN_TODO_TITLE = "todoTitle";
    private static final String COLUMN_TODO_CONTENT = "todoContent";
    private static final String COLUMN_TODO_CATEGORY_ID = "todoCategoryID";
    private static final String COLUMN_TODO_USER_ID = "todoUserID";
    private static final String CREATE_TABLE_TODO =
            "CREATE TABLE " + TABLE_TODO + " (" +
                    COLUMN_TODO_ID + " INTEGER PRIMARY KEY , " +
                    COLUMN_TODO_TITLE + " TEXT , " +
                    COLUMN_TODO_CONTENT + " TEXT , " +
                    COLUMN_TODO_CATEGORY_ID + " INTEGER , " +
                    COLUMN_TODO_USER_ID + " INTEGER " +
                    ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_TODO);
        insertDummyIntoCategory(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDummyIntoCategory(SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(categoryName) VALUES ('Arbete'), ('Fritid'), ('Viktigt')");

    }

    public boolean createUser(String userName, String userPassword) {

        long success = 0;

        boolean userExist = checkIfuserExists(userName);

        if (!userExist) {

            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_USER_USER, userName);
            cv.put(COLUMN_USER_PASSWORD, userPassword);

            success = db.insert(TABLE_USERS, null, cv);

            db.close();
        }
        return success > 0;

    }

    public boolean loginUser(String userName, String userPassword) {

        List<User> users = getAllUsers();
        boolean check = false;


        if (!users.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                User tempUser = users.get(i);
                if (tempUser.getUserName().equals(userName)) {
                    if (tempUser.getUserPassword().equals(userPassword)) {
                        currentUser = tempUser;
                        check = true;
                        break;
                    }
                }
            }
        }
        return check;
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(DbHelper.TABLE_USERS, null, null, null, null, null, null);
        boolean success = c.moveToFirst();

        if (success) {
            do {
                User user = new User();
                user.setUserId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_USER_ID)));
                user.setUserName(c.getString(c.getColumnIndex(DbHelper.COLUMN_USER_USER)));
                user.setUserPassword(c.getString(c.getColumnIndex(DbHelper.COLUMN_USER_PASSWORD)));
                users.add(user);
            } while (c.moveToNext());
        }

        db.close();

        return users;
    }

    public User getCurrenctUser() {
        return currentUser;
    }

    public List<String> getAllUserNames() {
        SQLiteDatabase db = getReadableDatabase();

        List<String> userNames = new ArrayList<>();

        Cursor c = db.query(DbHelper.TABLE_USERS, null, null, null, null, null, null);
        boolean success = c.moveToFirst();
        if (success) {
            while (c.moveToNext()) {
                String user = c.getString(c.getColumnIndex(DbHelper.COLUMN_USER_USER));
                userNames.add(user);
            }
        }
        return userNames;
    }

    public boolean checkIfuserExists(String newUser) {
        boolean b = false;

        List<String> userNames = getAllUserNames();
        for (int i = 0; i < userNames.size(); i++) {
            if (userNames.get(i).equals(newUser)) {
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean createTodo(String todoTitle, String todoContent, int todoCategoryID, int todoUserId) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TODO_TITLE, todoTitle);
        cv.put(COLUMN_TODO_CONTENT, todoContent);
        cv.put(COLUMN_TODO_CATEGORY_ID, todoCategoryID);
        cv.put(COLUMN_TODO_USER_ID, todoUserId);

        long success = db.insert(TABLE_TODO, null, cv);
        db.close();

        return success >= 0;
    }

    public boolean deleteTodo(int todoId) {
        boolean check = false;
        SQLiteDatabase db = getWritableDatabase();

        String query = "DELETE FROM Todo WHERE todoID = " + todoId +";";
        db.execSQL(query);
        return check;
    }

    public List<Todo> getAllTodos(int userId) {
        List<Todo> userTodos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_TODO, null, null, null, null, null, null);

        boolean success = c.moveToFirst();
        if (success) {
            do {
                Todo todo = new Todo();
                todo.setTodoId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_ID)));
                todo.setTodoTitle(c.getString(c.getColumnIndex(DbHelper.COLUMN_TODO_TITLE)));
                todo.setTodoContent(c.getString(c.getColumnIndex(DbHelper.COLUMN_TODO_CONTENT)));
                todo.setTodoCategoryId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_CATEGORY_ID)));
                todo.setTodoUserId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_USER_ID)));
                if (c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_USER_ID)) == userId) {
                    userTodos.add(todo);
                }
            } while (c.moveToNext());
        }
        db.close();
        return userTodos;
    }

    public Todo getSpecificTodo(int toDoId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_TODO, null, null, null, null, null, null);
        Todo todo = new Todo();
        todo.setTodoId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_ID)));
        todo.setTodoTitle(c.getString(c.getColumnIndex(DbHelper.COLUMN_TODO_TITLE)));
        todo.setTodoContent(c.getString(c.getColumnIndex(DbHelper.COLUMN_TODO_CONTENT)));
        todo.setTodoCategoryId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_CATEGORY_ID)));
        todo.setTodoUserId(c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_USER_ID)));
        if (c.getInt(c.getColumnIndex(DbHelper.COLUMN_TODO_ID)) == toDoId) {
            return todo;
        }
        db.close();
        return null;
    }
}

