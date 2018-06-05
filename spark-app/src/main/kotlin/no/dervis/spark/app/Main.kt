@file:JvmName("Main")
package no.dervis.spark.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.eclipse.jetty.http.HttpStatus
import spark.Spark.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val Id = AtomicInteger()

fun main(args: Array<String>) {
    println("Starting ToDo Spark-app.")
    val todos = ToDoList(mutableListOf())
    val jackson = jacksonObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true)
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ"))

    val contentTypeJson = "application/json;charset=utf-8"

    todos.add(ToDoItem(title = "Test1", done = false))
    todos.add(ToDoItem(title = "Test2", done = false))

    path("/") {
        get("") { _, _ ->
            "ToDo App"
        }
    }

    path("/todo") {
        get("") {_, response ->
            response.type(contentTypeJson)
            jackson.writeValueAsString(todos)
        }

        get("/:id") { request, response ->
            val responseItem = todos.get(request.params(":id").toInt())
            response.type(contentTypeJson)
            jackson.writeValueAsString(responseItem)
        }

        post("/") { request, response ->
            val toDo = jackson.readValue(request.body(), ToDoItem::class.java)
            todos.add(toDo)
            response.status(HttpStatus.CREATED_201)
            response.type(contentTypeJson)
            jackson.writeValueAsString(Entity(uri = "/todo/${toDo.id}"))
        }

        put("/:id") { request, response ->
            val responseItem = jackson.readValue(request.body(), ToDoItem::class.java)

            todos.update(request.params(":id").toInt(), responseItem)
            response.status(HttpStatus.OK_200)
            "ok"
        }

        delete("/:id") { request, response ->
            todos.delete(request.params(":id").toInt())
            response.status(HttpStatus.OK_200)
            "ok"
        }

        exception(JsonMappingException::class.java) { exception, _, response ->
            response.status(HttpStatus.BAD_REQUEST_400)
            response.type(contentTypeJson)
            response.body(jackson.writeValueAsString(Error(errorMessage = "Invalid input", exception = exception)))
        }

        exception(ItemNotFoundError::class.java) { exception, _, response ->
            response.status(HttpStatus.NOT_FOUND_404)
            response.type(contentTypeJson)
            response.body(jackson.writeValueAsString(exception))
        }
    }
}
