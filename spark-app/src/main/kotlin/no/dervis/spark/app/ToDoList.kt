package no.dervis.spark.app

data class ToDoList(val todoList: MutableList<ToDoItem>) {
    private fun lookUpItem(id: Int): ToDoItem? = todoList.getOrNull(id)
    fun get(id: Int): ToDoItem? = lookUpItem(id) ?: throw ItemNotFoundError(errorMessage = "Item was not found.")
    fun add(toDo: ToDoItem) = todoList.add(toDo)
    fun delete(id: Int) = todoList.removeAt(id)
    fun update(id: Int, toDo: ToDoItem) {
        val oldTodo = lookUpItem(id) ?: throw ItemNotFoundError("Item was not found.")
        todoList[id] = oldTodo.copy(
                title = toDo.title,
                dueDateTime = toDo.dueDateTime,
                done = toDo.done)
    }
}