package no.dervis.java.app;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World, from Java-App.");

        ToDoService toDoService = new ToDoService("http://localhost:4567");

        ToDo toDo = new ToDo("My new todo", false, new Date());

        Entity entity = toDoService.createTodo(toDo);
    }

}
