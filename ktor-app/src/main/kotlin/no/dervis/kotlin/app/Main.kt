@file:JvmName("Main")
package no.dervis.kotlin.app

import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

// vararg args: String
fun main(args: Array<String>) {
    println("Starting Ktor-app.")
    embeddedServer(Jetty, commandLineEnvironment(args)).start()
}
