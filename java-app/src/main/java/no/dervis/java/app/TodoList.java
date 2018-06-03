package no.dervis.java.app;

import java.util.List;

public class TodoList {

    private List<ToDo> todoList;

    public TodoList() {
    }

    public List<ToDo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<ToDo> todoList) {
        this.todoList = todoList;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "todoList=" + todoList +
                '}';
    }
}
