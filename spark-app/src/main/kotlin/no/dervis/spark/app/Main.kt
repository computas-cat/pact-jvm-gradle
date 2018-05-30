@file:JvmName("Main")
package no.dervis.spark.app

import spark.Spark.get

data class ToDo(val done: Boolean, val email: String, val id: Int)


// vararg args: String
fun main(args: Array<String>) {
    println("Starting Spark-app.")
    get("/hello") { req, res -> "Hello World" }
}

