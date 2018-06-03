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

data class Error(val message: String, val error: Exception? = null)

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
    fun update(toDo: ToDoItem) =  if (toDo.id >= 0) todoList.set(toDo.id, toDo) else throw IllegalArgumentException()
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

        post("") { request, response ->
            todos.add(jackson.readValue(request.body(), ToDoItem::class.java))
            response.status(201)
            "ok"
        }

        put("") { request, response ->
            val responseItem = jackson.readValue(request.body(), ToDoItem::class.java)
            todos.update(responseItem)
            response.status(200)
            "Ok"
        }

        delete("/:id") { request, response ->
            todos.delete(request.params(":id").toInt())
            response.status(200)
            "Ok"
        }
    }
}
