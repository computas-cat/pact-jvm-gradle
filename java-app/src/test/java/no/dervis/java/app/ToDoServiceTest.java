package no.dervis.java.app;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;

@ExtendWith(PactConsumerTestExt.class)
public class ToDoServiceTest {

    private static final String TODO_API_URL = "/todo";
    private static final String CONSUMER = "java-app";
    private static final String PROVIDER = "spark-app";


    /**
     *
     * A Todo-list exists
     * Read the following thread to understand multiple expected
     * interaction on one path: https://github.com/pact-foundation/pact-js/pull/95
     *
     */

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasTodoList(PactDslWithProvider builder) {
        Map<String, String> headers = Collections.singletonMap("Content-Type", "application/json;charset=utf-8");

        return builder
                .given("a todo list exists")
                .uponReceiving("a request for a list of todo's")
                .path(TODO_API_URL)
                .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(newJsonBody(body -> {
                        body.minArrayLike("todoList", 1, todo -> {
                            todo.id();
                            todo.stringType("title");
                            todo.date("dueDateTime", "yyyy-MM-dd'T'HH:mm:ssZZ");
                            todo.booleanType("done");
                        });
                    }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasTodoList")
    public void verifyHasTodoList(MockServer mockServer) throws IOException {
        ToDoService toDoService = new ToDoService(mockServer.getUrl());
        List<ToDo> list = toDoService.getToDos();

        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(1, list.size());
    }


    /**
     *
     * One Todo exists
     *
     */

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasOneTodo(PactDslWithProvider builder) {
        Map<String, String> headers = Collections.singletonMap("Content-Type", "application/json;charset=utf-8");

        return builder
                .given("a todo item exists")
                .uponReceiving("a request for a todo item that exist")
                .path(String.format("%s/%s", TODO_API_URL, 0))
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(newJsonBody(body -> {
                    body.numberType("id", 0);
                    body.stringType("title", "Example");
                    body.date("dueDateTime", "yyyy-MM-dd'T'HH:mm:ssZZ");
                    body.booleanType("done", false);
                }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasOneTodo")
    void verifyHasOneTodo(MockServer mockServer) throws IOException {
        ToDoService toDoService = new ToDoService(mockServer.getUrl());
        ToDo todoItem = toDoService.getToDo(0);

        Assertions.assertNotNull(todoItem);
        Assertions.assertEquals(0, todoItem.getId().intValue());
        Assertions.assertEquals("Example", todoItem.getTitle());
        Assertions.assertFalse(todoItem.isDone());
    }

    @Test
    void verifyCanCreateTodo(MockServer mockServer) throws IOException {
        ToDoService toDoService = new ToDoService(mockServer.getUrl());

        ToDo toDo = new ToDo("My new todo", false, new Date());

        Entity entity = toDoService.createTodo(toDo);

        Assertions.assertNotNull(entity);
        Assertions.assertNull(entity.getUri());
    }

}
