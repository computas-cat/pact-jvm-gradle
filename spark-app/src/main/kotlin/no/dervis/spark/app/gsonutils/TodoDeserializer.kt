package no.dervis.spark.app.gsonutils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import no.dervis.spark.app.Id
import no.dervis.spark.app.ToDoItem
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TodoDeserializer : JsonDeserializer<ToDoItem> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ToDoItem {
        val root = json as JsonObject;

        val id = root.get("id")?.asInt ?: Id.getAndIncrement()

        val title = root.get("title")?.asString ?: "My custom todo $id"

        val dueDateTime = Date.from(
                LocalDateTime.parse(
                        root.get("dueDateTime")?.asString
                                ?: LocalDateTime.now().toString()).atZone(ZoneId.systemDefault()).toInstant())
        val done = root.get("done")?.asBoolean ?: false

        return ToDoItem(id, title, dueDateTime, done)
    }
}