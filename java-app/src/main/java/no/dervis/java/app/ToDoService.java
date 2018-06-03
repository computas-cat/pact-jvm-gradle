package no.dervis.java.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public class ToDoService {

    private String host;
    private String endpoint = "/todo";


    public ToDoService(String host) {
        this.host = host;
    }

    public List<ToDo> getToDos() throws IOException {
        Response response = Request.Get(host + endpoint).execute();
        TodoList todoList = new ObjectMapper().readValue(response.returnContent().asString(), TodoList.class);
        System.out.println(todoList);

        return todoList.getTodoList();
    }

    public ToDo getToDos(ZonedDateTime from, ZonedDateTime to) {

        return null;
    }

    public Response createTodo(ToDo toDo) {
        Response response = null;
        try {
            response = Request.Post(host + endpoint).bodyString(
                    new ObjectMapper().writeValueAsString(toDo), ContentType.APPLICATION_JSON
            ).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
