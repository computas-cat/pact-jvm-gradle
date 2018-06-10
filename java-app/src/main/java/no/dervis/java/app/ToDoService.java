package no.dervis.java.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    public Entity createTodo(ToDo toDo) {
        Response response = null;
        Entity entity = null;

        try {
            final ObjectMapper jackson = new ObjectMapper()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"));

            response = Request.Post(host + endpoint)
                    .body(new StringEntity(jackson.writeValueAsString(toDo), ContentType.APPLICATION_JSON))
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .execute();

            HttpResponse httpResponse = response.returnResponse();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
            String content = new String(bytes);

            if (statusCode != HttpStatus.SC_CREATED) {
                throw new RuntimeException(content);
            }

            if (bytes != null && !content.isEmpty()) {
                entity = jackson.readValue(content, Entity.class);
                System.out.println(entity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return entity;
    }
}
