package no.dervis.kotlin.app

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title
import java.text.DateFormat

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            call.respondHtml {
                head {
                    title { +"Ktor: jetty-embedded" }
                }
                body {
                    p {
                        +"Hello from Ktor Jetty embedded engine sample application"
                    }
                }
            }
        }
    }
}