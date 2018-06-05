package no.dervis.spark.app

data class ToDoList(val todoList: MutableList<ToDoItem>) : ToDoStorage {
    private fun lookUpItem(id: Int): ToDoItem? = todoList.getOrNull(id)
    override fun get(id: Int): ToDoItem? = lookUpItem(id) ?: throw ItemNotFoundError(errorMessage = "Item was not found.")
    override fun add(toDo: ToDoItem): Boolean = todoList.add(toDo)
    override fun delete(id: Int): ToDoItem = todoList.removeAt(id)
    override fun update(id: Int, toDo: ToDoItem) {
        val oldTodo = lookUpItem(id) ?: throw ItemNotFoundError("Item was not found.")
        todoList[id] = oldTodo.copy(
                title = toDo.title,
                dueDateTime = toDo.dueDateTime,
                done = toDo.done)
    }
}