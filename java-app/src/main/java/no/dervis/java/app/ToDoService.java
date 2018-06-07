package no.dervis.java.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
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

    public ToDo getToDo(int id) throws IOException {
        Response response = Request.Get(host + endpoint + "/" + id).execute();
        ToDo todoItem = new ObjectMapper().readValue(response.returnContent().asString(), ToDo.class);
        System.out.println(todoItem);

        return todoItem;
    }

    public boolean updateTodo(ToDo toDo) {
        Response response = null;

        try {
            response = Request.Put(host + endpoint).bodyString(
                    new ObjectMapper().writeValueAsString(toDo), ContentType.APPLICATION_JSON
            ).execute();
            int statusCode = response.returnResponse().getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(response.returnContent().asString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean createTodo(ToDo toDo) {
        Response response = null;

        try {
            final ObjectMapper jackson = new ObjectMapper();
            response = Request.Post(host + endpoint).bodyString(
                    jackson.writeValueAsString(toDo), ContentType.APPLICATION_JSON
            ).execute();

            int statusCode = response.returnResponse().getStatusLine().getStatusCode();
            String content = response.returnContent().asString();

            if (statusCode != HttpStatus.SC_CREATED) {
                throw new RuntimeException(content);
            }

            if (content != null && !content.isEmpty()) {
                System.out.println(jackson.readValue(content, Entity.class));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private class Entity {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "uri='" + uri + '\'' +
                    '}';
        }
    }
}
