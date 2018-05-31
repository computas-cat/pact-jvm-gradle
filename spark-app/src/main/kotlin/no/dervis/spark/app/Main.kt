@file:JvmName("Main")
package no.dervis.spark.app

import spark.Spark.get

data class ToDo(val id: Int, val description: String, val done: Boolean)

class ToDos {
    val list = mutableListOf<ToDo>()

}


// vararg args: String
fun main(args: Array<String>) {
    println("Starting Spark-app.")
    get("/hello") { req, res -> "Hello World" }
}

