package id.my.hendisantika.service

import id.my.hendisantika.config.DatabaseFactory
import id.my.hendisantika.model.Book
import id.my.hendisantika.model.BookRequest
import id.my.hendisantika.model.Books
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.sortedBy
import org.ktorm.entity.toList

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
    private val database = DatabaseFactory.database

    fun createBook(bookRequest: BookRequest): Boolean {
        val newBook = Book {
            name = bookRequest.name
        }

        val affectedRecordsNumber =
            database.sequenceOf(Books)
                .add(newBook)

        return affectedRecordsNumber == 1
    }

    fun findAllBooks(): List<Book> =
        database.sequenceOf(Books)
            .sortedBy { book -> book.id }
            .toList()

    fun findBookById(bookId: Long): Book? =
        database.sequenceOf(Books)
            .find { book -> book.id eq bookId }

    fun updateBookById(bookId: Long, bookRequest: BookRequest): Boolean {
        val foundBook = findBookById(bookId)
        foundBook?.name = bookRequest.name

        val affectedRecordsNumber = foundBook?.flushChanges()

        return affectedRecordsNumber == 1
    }

    fun deleteBookById(bookId: Long): Boolean {
        val foundBook = findBookById(bookId)

        val affectedRecordsNumber = foundBook?.delete()

        return affectedRecordsNumber == 1
    }
}