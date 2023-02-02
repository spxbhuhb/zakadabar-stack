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
import zakadabar.lib.accounts.data.OauthSettings
import zakadabar.lib.accounts.jwt.JwtDecoder
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import java.net.URL

class Oauth2(oauth: OauthSettings, callback: (DecodedJWT)->StackSession) {

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
                val params = request.queryParameters[APP_PARAM]?.let { "?$APP_PARAM=$it" } ?: ""
                with(request.origin) {
                    "$scheme://$host:$port${oauth.callback}$params"
                }
            }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = oauth.name,
                    authorizeUrl = oauth.authorizeUrl,
                    accessTokenUrl = oauth.accessTokenUrl,
                    requestMethod = HttpMethod.Post,
                    clientId = oauth.clientId,
                    clientSecret = oauth.clientSecret ?: "",
                    defaultScopes = oauth.defaultScopes ?: emptyList(),
                )
            }
        }
    }

    val routeConfig = KtorRouteConfig {
        authenticate(configName) {
            get(oauth.login) {/* auto redirect to authorizeUrl by ktor */}
            get(oauth.callback) {
                val principal = call.principal() as? OAuthAccessTokenResponse.OAuth2 ?: throw Unauthorized("no token")
                val jwtBlob = principal.idToken ?: throw Unauthorized("no id_token")

                println("accessToken:\n${principal.accessToken}")
                println("idToken:\n$jwtBlob")

                val jwksUrl = oauth.jwksUrl?.let(::URL)
                val jwt = JwtDecoder(jwksUrl).decode(jwtBlob)

                val session = callback(jwt)

                call.sessions.set(session)

                val app = call.request.queryParameters[APP_PARAM]

                val url = when {
                    app == null -> "/"
                    oauth.containsApp(app) -> "$app?$SESSION_KEY=${call.sessionKey}&$SESSION_ID=${call.sessionId}"
                    else -> throw Forbidden()
                }

                call.respondRedirect(url)
            }
        }
    }

    companion object {
        const val ROOT = "/api/auth"
        const val APP_PARAM = "app"
        const val SESSION_KEY = "sessionKey"
        const val SESSION_ID = "sessionId"

        val OauthSettings.login : String get() = "$ROOT/$name/login"
        val OauthSettings.callback : String get() = "$ROOT/$name/callback"
        fun OauthSettings.containsApp(app: String) = externalApps?.contains(app) == true

        private val ApplicationCall.sessionKey : String get() = sessions.findName(StackSession::class)
        private val ApplicationCall.sessionId : String? get() = request.cookies[sessionKey]
        private val OAuthAccessTokenResponse.OAuth2.idToken : String? get() = extraParameters["id_token"]

        @Suppress("CustomX509TrustManager")
        private object EmptyX509TrustManager: X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
            override fun checkServerTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
            override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
        }

    }

}

