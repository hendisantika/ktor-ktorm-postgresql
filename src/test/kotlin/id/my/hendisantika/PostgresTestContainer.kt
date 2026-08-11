package id.my.hendisantika

import org.testcontainers.postgresql.PostgreSQLContainer
import java.sql.DriverManager
import java.util.logging.Logger

/**
 * This class provides database connection properties for tests using Testcontainers.
 * It starts a PostgreSQL container for testing, which makes tests portable across environments.
 */
open class PostgresTestContainer {

    /**
     * Drops the `book` table and re-applies `init-test-db.sql`, so every test
     * starts from the same three rows with ids 1..3.
     */
    protected fun initializeTestDatabase() {
        val connection = DriverManager.getConnection(
            System.getProperty("DB_URL"),
            System.getProperty("DB_USER"),
            System.getProperty("DB_PASSWORD")
        )

        connection.use { conn ->
            // First clean the database
            conn.createStatement().use { stmt ->
                stmt.execute("DROP TABLE if EXISTS book")
            }

            // Then initialize with fresh data
            val scriptContent =
                javaClass.classLoader.getResourceAsStream("init-test-db.sql")?.bufferedReader()?.readText()
                    ?: throw RuntimeException("Could not load init-test-db.sql")

            conn.createStatement().use { stmt ->
                stmt.execute(scriptContent)
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(PostgresTestContainer::class.java.name)

        // Singleton container: started once for the whole test JVM and reaped by
        // Ryuk on exit. A JUnit-managed @Container would be stopped after the
        // first test class, breaking every class that runs after it.
        private val postgresContainer = PostgreSQLContainer("postgres:17.5-alpine3.22").apply {
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
