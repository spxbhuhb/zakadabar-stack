/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

@Serializable
class ModuleSettings(

    /**
     * Maximum number of failed logins before the system locks the account.
     */
    var maxFailedLogins : Int = 5,

    /**
     * Session timeout in minutes.
     */
    var sessionTimeout: Long = 30,

    /**
     * Session record updates are delayed by this interval if only the time
     * of last activity changes. This reduces the number of database updates.
     * Seconds.
     */
    var updateDelay: Long = 120,

    /**
     * Session expiration check run interval. Expired sessions are removed
     * from the in-memory cache and from the database by this check. Seconds.
     */
    var expirationCheckInterval: Long = 120,

    /**
     * Initial password of the SO account. Used only during first time DB
     * initialization.
     */
    var initialSoPassword: String? = null,

    /**
     * When true email addresses are put into AccountPublicBo objects.
     * Default is false.
     */
     var emailInAccountPublic : Boolean = false,

    /**
     * When not empty, this role is required for the user to execute the
     * LoginAction.
     */
    var loginActionRole : String = ""

) : BaseBo {

    override fun schema() = BoSchema {
        + ::maxFailedLogins default maxFailedLogins
        + ::sessionTimeout default sessionTimeout
        + ::updateDelay default updateDelay
        + ::expirationCheckInterval default expirationCheckInterval
        + ::initialSoPassword default initialSoPassword
        + ::emailInAccountPublic default emailInAccountPublic
        + ::loginActionRole default loginActionRole
    }

}