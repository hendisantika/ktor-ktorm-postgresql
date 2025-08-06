package id.my.hendisantika

import org.testcontainers.junit.jupiter.Testcontainers
import java.util.logging.Logger

/**
 * This class provides database connection properties for tests.
 * It's designed to work with the existing PostgreSQL instance from compose.yml
 * rather than starting a test container, which simplifies the setup.
 */
@Testcontainers
open class PostgresTestContainer {

    companion object {
        private val logger = Logger.getLogger(PostgresTestContainer::class.java.name)

        init {
            logger.info("Setting up database connection properties for tests")

            // Use the existing PostgreSQL instance from compose.yml
            System.setProperty("DB_URL", "jdbc:postgresql://localhost:5438/postgres")
            System.setProperty("DB_USER", "yu71")
            System.setProperty("DB_PASSWORD", "53cret")
        }
    }
}