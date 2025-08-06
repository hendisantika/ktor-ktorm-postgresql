package id.my.hendisantika.id.my.hendisantika.route

import id.my.hendisantika.id.my.hendisantika.model.Book
import id.my.hendisantika.id.my.hendisantika.model.BookResponse

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.41
 * To change this template use File | Settings | File Templates.
 */
private fun Book?.toBookResponse(): BookResponse? =
    this?.let { BookResponse(it.id!!, it.name) }