package no.dervis.spark.app

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class ToDoItem(
        val id: Int = Id.getAndIncrement(),
        val title: String,
        val dueDateTime: Date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
        val done: Boolean)