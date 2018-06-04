@file:JvmName("Main")
package no.dervis.spark.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.gson.GsonBuilder
import org.eclipse.jetty.http.HttpStatus
import spark.Spark.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val Id = AtomicInteger()

object Json {
    private val gson = GsonBuilder()
            .registerTypeAdapter(ToDoItem::class.java, TodoDeserializer())
            .registerTypeAdapter(Date::class.java, ISO8601Date())
            .setPrettyPrinting()
            .create()
    fun <T> fromJson(json: String, t: Class<T>): T = gson.fromJson(json, t)
    fun <T> toJson(t: T) = gson.toJson(t)
}

data class Error(val message: String, val error: String? = "") { constructor() : this("") }

data class ToDoItem(
        val id: Int = Id.getAndIncrement(),
        val title: String,
        val dueDateTime: Date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
        val done: Boolean)

data class ToDoList(
        val todoList: MutableList<ToDoItem>) {

    fun get(id: Int): ToDoItem? = todoList.getOrNull(id)
    fun add(toDo: ToDoItem) = todoList.add(toDo)
    fun delete(id: Int) = todoList.removeAt(id)
    fun update(id: Int, toDo: ToDoItem) =  {
        val oldTodo = get(id) ?: Error()
        if (oldTodo is Error) throw IllegalArgumentException("Item with id $id was not found")
    }
}

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
            val responseItem = todos.get(request.params(":id").toInt()) ?: Error(message = "Item was not found.")
            if (responseItem is Error) response.status(HttpStatus.NOT_FOUND_404)
            jackson.writeValueAsString(responseItem)
        }

        post("/") { request, response ->
            todos.add(jackson.readValue(request.body(), ToDoItem::class.java))
            response.status(201)
            "ok"
        }

        put("/:id") { request, response ->
            val responseItem = try {
                jackson.readValue(request.body(), ToDoItem::class.java)
            }
            catch (e: Exception) {
                Error(message = "Invalid input.", error = e.message)
            }

            when (responseItem) {
                is Error -> {
                    response.status(HttpStatus.BAD_REQUEST_400)
                    jackson.writeValueAsString(responseItem)
                }
                is ToDoItem -> {
                    todos.update(request.params(":id").toInt(), responseItem)
                    response.status(200)
                    "Ok"
                }
                else -> throw IllegalStateException()
            }
        }

        delete("/:id") { request, response ->
            todos.delete(request.params(":id").toInt())
            response.status(200)
            "Ok"
        }
    }
}
