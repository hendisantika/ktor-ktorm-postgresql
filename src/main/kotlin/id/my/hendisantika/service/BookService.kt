package id.my.hendisantika.id.my.hendisantika.service

import org.ktorm.database.Database

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.39
 * To change this template use File | Settings | File Templates.
 */
class BookService {
    private val database = Database.connect(
        url = "jdbc:postgresql://localhost:5438/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}