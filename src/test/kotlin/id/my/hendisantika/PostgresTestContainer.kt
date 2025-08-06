package id.my.hendisantika

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.logging.Logger

/**
 * This class provides database connection properties for tests using Testcontainers.
 * It starts a PostgreSQL container for testing, which makes tests portable across environments.
 */
@Testcontainers
open class PostgresTestContainer {

    companion object {
        private val logger = Logger.getLogger(PostgresTestContainer::class.java.name)

        @Container
        private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:17.5-alpine3.22").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpass")
            start()
        }

        init {
            logger.info("Setting up database connection properties for tests with Testcontainers")

            // Use the PostgreSQL container for tests
            System.setProperty("DB_URL", postgresContainer.jdbcUrl)
            System.setProperty("DB_USER", postgresContainer.username)
            System.setProperty("DB_PASSWORD", postgresContainer.password)
        }
    }
}