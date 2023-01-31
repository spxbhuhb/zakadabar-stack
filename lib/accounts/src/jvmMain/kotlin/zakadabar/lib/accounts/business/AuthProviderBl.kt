/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.business

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.QueryBo
import zakadabar.core.exception.Forbidden
import zakadabar.core.exception.Unauthorized
import zakadabar.core.module.module
import zakadabar.core.server.ktor.KtorAuthConfig
import zakadabar.core.server.ktor.KtorRouteConfig
import zakadabar.core.server.ktor.plusAssign
import zakadabar.core.setting.setting
import zakadabar.core.server.server
import zakadabar.lib.accounts.data.*
import zakadabar.lib.accounts.server.ktor.JwtDecoder
import zakadabar.lib.accounts.server.ktor.StackSession
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class AuthProviderBl(
    override val namespace: String = AuthProviderBo.boNamespace
) : BusinessLogicCommon<AuthProviderBo>() {


    private val settings by setting<ModuleSettings>()

    private val accountBl by module<AccountBlProvider>()

    override val authorizer = object : BusinessLogicAuthorizer<AuthProviderBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            // everyone can query auth providers
        }
    }

    override val router = router {
        query(AuthProviderList::class, ::authProviderList)
    }

    private val OauthSettings.login : String get() = "/api/auth/$name/login"
    private val OauthSettings.callback : String get() = "/api/auth/$name/callback"

    override fun onModuleStart() {
        super.onModuleStart()

        settings.oauth?.forEach { oauth->

            println("Register Oauth: ${oauth.name} - ${oauth.displayName}")

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

            server += KtorAuthConfig {
                oauth(configName) {
                    client = httpClient
                    urlProvider = {
                        val params = request.queryParameters["app"]?.let { "?app=$it" } ?: ""
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

            server += KtorRouteConfig {
                authenticate(configName) {
                    get(oauth.login) { /* auto redirect to authorizeUrl by ktor */}
                    get(oauth.callback) {
                        val principal = call.principal() as? OAuthAccessTokenResponse.OAuth2 ?: throw Unauthorized("no token")
                        val jwtBlob = principal.accessToken
                        val jwksUrl = oauth.jwksUrl?.let(::URL)
                        val jwt = JwtDecoder(jwksUrl).decode(jwtBlob)

                        println(jwt.claims)

                        val accountName = jwt.getClaim("email").asString()
                        val roles = jwt.getClaim("roles").asArray(String::class.java)

                        val executor : Executor = try {
                            accountBl.executorFor(accountName)
                        } catch(ex: Exception) {
                            if (oauth.autoRegister) register()
                            else throw Unauthorized("account not found: $accountName")
                        }

                        println("Executor: ${executor.accountId}")
                        //TODO: sync roles

                        call.sessions.set(executor.toSession())

                        val sessionKey = call.sessions.findName(StackSession::class)
                        val sessionId = call.request.cookies[sessionKey]

                        val app = call.request.queryParameters["app"]

                        if (app == null) {
                            call.respondRedirect("/")
                        } else if (oauth.externalApps?.contains(app) == true) {
                            call.respondRedirect("$app?sessionKey=$sessionKey&sessionId=$sessionId")
                        } else {
                            throw Forbidden()
                        }

                    }
                }
            }

        }

    }

    private fun register() : Executor {
        TODO("not implemented yet")
    }

    private fun authProviderList(executor: Executor, query: AuthProviderList) : List<AuthProviderBo> {
        return settings.oauth?.map { AuthProviderBo(
            it.name,
            it.displayName,
            it.login,
            it.svgIcon
        ) } ?: emptyList()
    }

    private fun Executor.toSession() = StackSession(
        account = accountId,
        accountUuid = accountUuid,
        anonymous = anonymous,
        roleIds = roleIds,
        roleNames = roleNames,
        permissionIds = permissionIds,
        permissionNames = permissionNames
    )

    private object EmptyX509TrustManager: X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
        override fun checkServerTrusted(p0: Array<out X509Certificate>, p1: String) = Unit
        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }

}