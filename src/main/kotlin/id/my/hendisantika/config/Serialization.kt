package id.my.hendisantika.id.my.hendisantika.config

import io.ktor.serialization.*
import io.ktor.server.application.*

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.33
 * To change this template use File | Settings | File Templates.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}