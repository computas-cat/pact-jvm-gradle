@file:JvmName("Main")
package no.dervis.spark.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.eclipse.jetty.http.HttpStatus
import spark.Spark.*
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
            .setDateFormat(StdDateFormat().withTimeZone(TimeZone.getDefault()))

    todos.add(ToDoItem(title = "Test1", done = false))
    todos.add(ToDoItem(title = "Test2", done = false))

    path("/") {
        get("") { _, _ ->
            "ToDo App"
        }
    }

    path("/todo") {
        get("") {_, _ -> jackson.writeValueAsString(todos) }

        get("/:id") { request, response ->
            val responseItem = todos.get(request.params(":id").toInt())
            jackson.writeValueAsString(responseItem)
        }

        post("/") { request, response ->
            todos.add(jackson.readValue(request.body(), ToDoItem::class.java))
            response.status(201)
            "ok"
        }

        put("/:id") { request, response ->
            val responseItem = jackson.readValue(request.body(), ToDoItem::class.java)

            todos.update(request.params(":id").toInt(), responseItem)
            response.status(200)
            "Ok"
        }

        delete("/:id") { request, response ->
            todos.delete(request.params(":id").toInt())
            response.status(200)
            "Ok"
        }

        exception(JsonMappingException::class.java) { exception, _, response ->
            response.status(HttpStatus.BAD_REQUEST_400)
            response.body(jackson.writeValueAsString(Error(errorMessage = "Invalid input", exception = exception)))
        }

        exception(ItemNotFoundError::class.java) { exception, _, response ->
            response.status(HttpStatus.NOT_FOUND_404)
            response.body(jackson.writeValueAsString(exception))
        }
    }
}
