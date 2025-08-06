package id.my.hendisantika.model

import kotlinx.serialization.Serializable

/**
 * Created by IntelliJ IDEA.
 * Project : ktor-ktorm-postgresql
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 06/08/25
 * Time: 10.37
 * To change this template use File | Settings | File Templates.
 */
@Serializable
data class ErrorResponse(val message: String)
