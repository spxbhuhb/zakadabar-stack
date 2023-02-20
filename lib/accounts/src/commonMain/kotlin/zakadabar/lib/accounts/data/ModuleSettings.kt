/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

@Serializable
class ModuleSettings(

    /**
     * Maximum number of failed logins before the system locks the account.
     */
    var maxFailedLogins: Int = 5,

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
     * When not empty, this role is required for the user to execute the
     * LoginAction.
     */
    var loginActionRole: String = "",

    /**
     * Validate accounts automatically. When true no external validation
     * is required, the `validated` field of the account state is set
     * to `true` automatically.
     */
    var autoValidate: Boolean = true,

    /**
     * Enable the [AccountList] query to be run by the users. Default
     * is `false` (the list is not enabled by default).
     */
    var enableAccountList: Boolean = false,

    /**
     * When true email addresses are put into AccountPublicBo objects.
     * Default is false.
     */
    var emailInAccountPublic: Boolean = false,

    /**
     * When true phone numbers are put into AccountPublicBo objects.
     * Default is false.
     */
    var phoneInAccountPublic: Boolean = false,


    var oauth : List<OauthSettings>? = null


) : BaseBo {

    override fun schema() = BoSchema {
        + ::maxFailedLogins default maxFailedLogins
        + ::sessionTimeout default sessionTimeout
        + ::updateDelay default updateDelay
        + ::expirationCheckInterval default expirationCheckInterval
        + ::initialSoPassword default initialSoPassword
        + ::loginActionRole default loginActionRole
        + ::autoValidate default autoValidate
        + ::enableAccountList default enableAccountList
        + ::emailInAccountPublic default emailInAccountPublic
        + ::phoneInAccountPublic default phoneInAccountPublic
    }

}

@Serializable
data class OauthSettings(
    /**
     * an arbitrary unique string, appears in part of URL
     */
    var name : String,

    /**
     * appears as button or link text
     */
    var displayName : String,

    /**
     * appears as icon
     */
    var svgIcon : String? = null,

    /**
     * Oauth Endpoint for login screen
     */
    var authorizationEndpoint : String,

    /**
     * Endpoint to acquire accessToken or idToken
     */
    var tokenEndpoint : String,

    /**
     * POST or GET for token endpoint
     */
    var tokenRequestMethod: String,

    /**
     * JWKS uri for JWT cert validation
     */
    var jwksUri : String,

    /**
     * clientId from Auth Vendor
     */
    var clientId : String,

    /**
     * clientSecret from Auth Vendor
     */
    var clientSecret : String?,

    /**
     * required login scope eg: openid, email, profile, etc
     */
    var scopes : List<String>,

    /**
     * JWT claim mapping for AccountPrivate
     */
    var claims: AccountClaims,

    /**
     * if true, the authenticated user automatically created in local db
     */
    var autoRegister : Boolean = false,

    /**
     * allowed external app urls, eg. Android deep links
     */
    var externalApps : List<String>? = null

)

@Serializable
data class AccountClaims(
    /**
     * claim name, holds local accountName
     * eg.: "upn", "unique_name", "email"
     * it depends on vendor's settings
     */
    var accountName: String,

    /**
     * clame name, holds user full name
     * eg.: "name", "commonname"
     * it depends on vendor's settings
     */
    var fullName: String,

    /**
     * claim name, holds user email address
     * almost always is "email"
     * it depends on vendor's settings
     */
    var email: String
)
