package id.my.hendisantika.id.my.hendisantika

import id.my.hendisantika.id.my.hendisantika.config.configureSerialization
import id.my.hendisantika.id.my.hendisantika.route.configureBookRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureBookRoutes()
        configureSerialization()
    }.start(wait = true)
}

fun Application.module() {
    configureRouting()
}
