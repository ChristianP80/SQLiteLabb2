package com.example.christianpersson.labb2sqlite;

/**
 * Created by christianpersson on 2018-02-05.
 */

public class Todo {

    private String todoTitle;
    private String todoContent;
    private int todoCategoryId;
    private int todoUserId;
    private int todoId;

    public Todo(String todoTitle, String todoContent, int todoCategoryId, int todoUserId) {
        this.todoTitle = todoTitle;
        this.todoContent = todoContent;
        this.todoCategoryId = todoCategoryId;
        this.todoUserId = todoUserId;
    }

    public Todo() {

    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodoContent() {
        return todoContent;
    }

    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public int getTodoCategoryId() {
        return todoCategoryId;
    }

    public void setTodoCategoryId(int todoCategoryId) {
        this.todoCategoryId = todoCategoryId;
    }

    public int getTodoUserId() {
        return todoUserId;
    }

    public void setTodoUserId(int todoUserId) {
        this.todoUserId = todoUserId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

}
