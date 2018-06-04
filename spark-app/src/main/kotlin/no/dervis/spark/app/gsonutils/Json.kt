package no.dervis.spark.app.gsonutils

import com.google.gson.GsonBuilder
import no.dervis.spark.app.ToDoItem
import java.util.*

object Json {
    private val gson = GsonBuilder()
            .registerTypeAdapter(ToDoItem::class.java, TodoDeserializer())
            .registerTypeAdapter(Date::class.java, ISO8601Date())
            .setPrettyPrinting()
            .create()
    fun <T> fromJson(json: String, t: Class<T>): T = gson.fromJson(json, t)
    fun <T> toJson(t: T) = gson.toJson(t)
}