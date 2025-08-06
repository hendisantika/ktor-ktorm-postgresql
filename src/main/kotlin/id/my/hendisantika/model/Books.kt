package id.my.hendisantika.id.my.hendisantika.model

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar
import java.awt.print.Book

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.35
 * To change this template use File | Settings | File Templates.
 */
object Books : Table<Book>("book") {
    val id = long("id").primaryKey().bindTo(Book::id)
    val name = varchar("name").bindTo(Book::name)
}