package no.dervis.spark.app

interface ToDoStorage {
    fun get(id: Int): ToDoItem?
    fun add(toDo: ToDoItem): Boolean
    fun delete(id: Int): ToDoItem
    fun update(id: Int, toDo: ToDoItem)

}