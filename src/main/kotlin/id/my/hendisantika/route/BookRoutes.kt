package id.my.hendisantika.id.my.hendisantika.route

import id.my.hendisantika.id.my.hendisantika.model.Book
import id.my.hendisantika.id.my.hendisantika.model.BookRequest
import id.my.hendisantika.id.my.hendisantika.model.BookResponse
import id.my.hendisantika.id.my.hendisantika.model.ErrorResponse
import id.my.hendisantika.id.my.hendisantika.service.BookService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

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

fun Application.configureBookRoutes() {
    routing {
        route("/book") {
            val bookService = BookService()
            createBook(bookService)
            getAllBooksRoute(bookService)
            getBookByIdRoute(bookService)
            updateBookByIdRoute(bookService)
            deleteBookByIdRoute(bookService)
        }
    }
}

fun Route.createBook(bookService: BookService) {
    post {
        val request = call.receive<BookRequest>()

        val success = bookService.createBook(bookRequest = request)

        if (success)
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Cannot create book"))
    }
}