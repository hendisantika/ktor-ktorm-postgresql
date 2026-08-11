package id.my.hendisantika.route

import id.my.hendisantika.PostgresTestContainer
import id.my.hendisantika.module
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Drives the real routes over HTTP, backed by the Testcontainers database.
 */
class BookRoutesTest : PostgresTestContainer() {

    @BeforeEach
    fun setup() {
        initializeTestDatabase()
    }

    /**
     * Boots the application module explicitly against an empty config, so the
     * test host does not also load the modules declared in `application.yaml`.
     */
    private fun bookApi(block: suspend ApplicationTestBuilder.() -> Unit): Unit = testApplication {
        environment { config = MapApplicationConfig() }
        application { module() }
        block()
    }

    @Test
    fun `GET books returns all books`() = bookApi {
        val response = client.get("/books")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("The Great Gatsby"), body)
        assertTrue(body.contains("To Kill a Mockingbird"), body)
        assertTrue(body.contains("1984"), body)
    }

    @Test
    fun `GET books returns them ordered by id`() = bookApi {
        // Updating a row rewrites it at the end of the table's heap, so an
        // unordered scan hands book 1 back last.
        client.patch("/books/1") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Rewritten, so no longer first in the heap"}""")
        }
        client.post("/books") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Added last"}""")
        }

        val ids = Regex(""""id":(\d+)""")
            .findAll(client.get("/books").bodyAsText())
            .map { match -> match.groupValues[1].toLong() }
            .toList()

        assertEquals(ids.sorted(), ids, "Books came back as ${'$'}ids")
    }

    @Test
    fun `GET book by id returns the book`() = bookApi {
        val response = client.get("/books/1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"id":1,"name":"The Great Gatsby"}""", response.bodyAsText())
    }

    @Test
    fun `GET missing book returns 404`() = bookApi {
        val response = client.get("/books/999")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("""{"message":"Book with id [999] not found"}""", response.bodyAsText())
    }

    @Test
    fun `GET book with a non numeric id returns 400`() = bookApi {
        val response = client.get("/books/abc")

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("""{"message":"Invalid id"}""", response.bodyAsText())
    }

    @Test
    fun `POST books creates a book`() = bookApi {
        val response = client.post("/books") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Dune"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(client.get("/books").bodyAsText().contains("Dune"))
    }

    @Test
    fun `PATCH books updates a book`() = bookApi {
        val response = client.patch("/books/1") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Updated Title"}""")
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        assertEquals("""{"id":1,"name":"Updated Title"}""", client.get("/books/1").bodyAsText())
    }

    @Test
    fun `PATCH missing book returns 404`() = bookApi {
        val response = client.patch("/books/999") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Nothing here"}""")
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("""{"message":"Book with id [999] not found"}""", response.bodyAsText())
    }

    @Test
    fun `DELETE books removes a book`() = bookApi {
        val response = client.delete("/books/3")

        assertEquals(HttpStatusCode.NoContent, response.status)
        assertEquals(HttpStatusCode.NotFound, client.get("/books/3").status)
    }

    @Test
    fun `DELETE missing book returns 404`() = bookApi {
        val response = client.delete("/books/999")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("""{"message":"Book with id [999] not found"}""", response.bodyAsText())
    }
}
