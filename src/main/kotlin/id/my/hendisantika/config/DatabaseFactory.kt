package id.my.hendisantika.config

import org.ktorm.database.Database

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.30
 * To change this template use File | Settings | File Templates.
 */
object DatabaseFactory {

    private const val DEFAULT_URL = "jdbc:postgresql://localhost:5438/ktor_postgres"
    private const val DEFAULT_USER = "yu71"
    private const val DEFAULT_PASSWORD = "53cret"

    /**
     * Reads a setting from a system property first, then the environment,
     * falling back to the value used by `compose.yml`.
     */
    private fun setting(key: String, default: String): String =
        System.getProperty(key) ?: System.getenv(key) ?: default

    val database: Database by lazy {
        Database.connect(
            url = setting("DB_URL", DEFAULT_URL),
            driver = "org.postgresql.Driver",
            user = setting("DB_USER", DEFAULT_USER),
            password = setting("DB_PASSWORD", DEFAULT_PASSWORD)
        )
    }
}
