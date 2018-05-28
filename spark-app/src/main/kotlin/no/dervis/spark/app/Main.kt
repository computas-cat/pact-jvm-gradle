@file:JvmName("Main")
package no.dervis.spark.app

import spark.Spark.get

data class User(val name: String, val email: String, val id: Int)


// vararg args: String
fun main(args: Array<String>) {
    println("Starting Spark-app.")
    get("/hello") { req, res -> "Hello World" }
}

