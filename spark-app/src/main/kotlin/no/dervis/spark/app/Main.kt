@file:JvmName("Main")
package no.dervis.spark.app

import com.google.gson.GsonBuilder
import org.eclipse.jetty.http.HttpStatus
import spark.Spark.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object Json {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    fun <T> fromJson(json: String, t: Class<T>): T = gson.fromJson(json, t)
    fun <T> toJson(t: T) = gson.toJson(t)
}

val Id = AtomicInteger()

data class Error(val description: String, val error: Exception? = null)

data class ToDoItem(
        val id: Int = Id.getAndIncrement(),
        val description: String,
        val dueDateTime: Date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
        val done: Boolean)

data class ToDoList(
        val todoList: MutableList<ToDoItem>) {

    fun get(id: Int): ToDoItem? = todoList.getOrNull(id)
    fun add(toDo: ToDoItem) = todoList.add(toDo)
    //fun update(id: Int, toDo: ToDoItem) =

}

fun main(args: Array<String>) {
    println("Starting ToDo Spark-app.")
    val todos = ToDoList(mutableListOf())

    path("/") {
        get("") { _, _ ->
            "ToDo App"
        }
    }

    path("/todo") {
        get("") {_, _ -> Json.toJson(todos) }

        get("/:id") { request, response ->
            val responseItem = todos.get(request.params(":id").toInt()) ?: Error(description = "Item was not found.")
            if (responseItem is Error) response.status(HttpStatus.NOT_FOUND_404)
            Json.toJson(responseItem)
        }

        post("") { request, response ->
            todos.add(Json.fromJson(request.body(), ToDoItem::class.java))
            response.status(201)
            "ok"
        }
    }
}

