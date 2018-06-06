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
import java.util.List;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;

@ExtendWith(PactConsumerTestExt.class)
public class ToDoServiceTest {

    private static final String TODO_API_URL = "/todo";
    private static final String CONSUMER = "java-app";
    private static final String PROVIDER = "kotlin-app";

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasTodoList(PactDslWithProvider builder) {
        Map<String, String> headers = Collections.singletonMap("Content-Type", "application/json; charset=UTF-8");

        return builder
                .given("a todo list exists")
                .uponReceiving("a request for a list of todo's")
                .path(TODO_API_URL)
                .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(newJsonBody(body -> {
                        body.eachLike("todoList", 3, list -> {
                            list.id();
                            list.stringType("title");
                            list.date("dueDateTime", "yyyy-MM-dd'T'HH:mm:ssZZ");
                            list.booleanType("done");
                        });
                    }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasTodoList")
    void hasTodoList(MockServer mockServer) throws IOException {
        ToDoService toDoService = new ToDoService(mockServer.getUrl());
        List<ToDo> list = toDoService.getToDos();

        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(3, list.size());
    }

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasNoTodoList(PactDslWithProvider builder) {
        Map<String, String> headers = Collections.singletonMap("Content-Type", "application/json; charset=UTF-8");

        return builder
                .given("todo list does not exists")
                .uponReceiving("a request for a todo in an empty todo list")
                .path(TODO_API_URL)
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(newJsonBody(body -> body.array("todoList", list -> {})).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasNoTodoList")
    void hasNoTodoList(MockServer mockServer) throws IOException {
        ToDoService toDoService = new ToDoService(mockServer.getUrl());
        List<ToDo> list = toDoService.getToDos();

        Assertions.assertTrue(list.isEmpty());
        Assertions.assertEquals(0, list.size());
    }

}
