/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.server.ktor

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import zakadabar.core.exception.Forbidden
import zakadabar.core.exception.Unauthorized
import zakadabar.core.server.ktor.KtorAuthConfig
import zakadabar.core.server.ktor.KtorRouteConfig
import zakadabar.lib.accounts.data.*
import zakadabar.lib.accounts.jwt.JwtDecoder
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import java.net.URL

class Oauth2(oauth: OauthSettings, callback: (Token)->StackSession) {

    val configName = "auth-oauth-${oauth.name}"

    val httpClient = HttpClient(CIO) {
        engine {
            https {
                if (oauth.trustAllCerts) {
                    trustManager = EmptyX509TrustManager
                }
            }
        }
        developmentMode = false
    }

    val authConfig = KtorAuthConfig {
        oauth(configName) {
            client = httpClient
            urlProvider = {
                val app = request.queryParameters[APP_PARAM]

                val params = app?.let {
                    if (oauth.containsApp(app)) "?$APP_PARAM=$app"
                    else throw Forbidden()
                } ?: ""

                with(request.origin) {
                    "$scheme://$host:$port${oauth.callback}$params"
                }
            }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = oauth.name,
                    authorizeUrl = oauth.authorizationEndpoint,
                    accessTokenUrl = oauth.tokenEndpoint,
                    requestMethod = HttpMethod(oauth.tokenRequestMethod),
                    clientId = oauth.clientId,
                    clientSecret = oauth.clientSecret ?: "",
                    defaultScopes = oauth.scopes,
                )
            }
        }
    }

    val routeConfig = KtorRouteConfig {
        authenticate(configName) {
            get(oauth.login) {/* auto redirect to authorizeUrl by ktor */}
            get(oauth.callback) {
                val principal = call.principal() as? OAuthAccessTokenResponse.OAuth2 ?: throw Unauthorized("no token")
                val idTokenBlob = principal.idToken ?: throw Unauthorized("no $ID_TOKEN")

                val jwksUri = if (oauth.trustAllCerts) null else oauth.jwksUri?.let(::URL)
                val idToken = JwtDecoder(jwksUri).decode(idTokenBlob)

                val session = callback(Token(oauth, principal.accessToken, idToken))

                call.sessions.set(session)

                val app = call.request.queryParameters[APP_PARAM]

                when {
                    app == null -> call.respondText("<script>window.opener.$ZK_AUTH_LOGGED_IN_JS()</script>", ContentType.Text.Html)
                    oauth.containsApp(app) -> call.respondRedirect("$app?$SESSION_KEY=${call.sessionKey}&$SESSION_ID=${call.sessionId}")
                    else -> throw Forbidden()
                }

            }
        }
    }

    companion object {
        const val ROOT = "/api/auth"
        const val APP_PARAM = "app"
        const val SESSION_KEY = "sessionKey"
        const val SESSION_ID = "sessionId"
        const val ID_TOKEN = "id_token"

        val OauthSettings.login : String get() = "$ROOT/$name/login"
        val OauthSettings.callback : String get() = "$ROOT/$name/callback"
        fun OauthSettings.containsApp(app: String) = externalApps?.contains(app) == true

        private val ApplicationCall.sessionKey : String get() = sessions.findName(StackSession::class)
        private val ApplicationCall.sessionId : String? get() = response.cookies[sessionKey]?.value
        private val OAuthAccessTokenResponse.OAuth2.idToken : String? get() = extraParameters[ID_TOKEN]

        @Suppress("CustomX509TrustManager")
        private object EmptyX509TrustManager: X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
            override fun checkServerTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }

    }

    data class Token(
        val authSettings: OauthSettings,
        val accessToken: String,
        val idToken : DecodedJWT
    )

}

