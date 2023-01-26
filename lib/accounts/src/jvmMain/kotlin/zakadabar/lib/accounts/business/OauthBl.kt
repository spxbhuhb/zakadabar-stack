/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.business

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.routing.*
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.ktor.KtorAuthConfig
import zakadabar.core.server.ktor.KtorRouteConfig
import zakadabar.core.server.ktor.plusAssign
import zakadabar.lib.accounts.data.ModuleSettings
import zakadabar.core.setting.setting
import zakadabar.core.server.server

class OauthBl : RoutedModule {

    private val settings by setting<ModuleSettings>()

    override fun onModuleLoad() {
        super.onModuleLoad()

        settings.oauth?.forEach { oauth->

            val configName = "auth-oauth-${oauth.name}"
            val path = "auth/${oauth.name}"

            server += KtorAuthConfig {
                oauth(configName) {
                    urlProvider = { "http://localhost:8080/$path/callback" }
                    providerLookup = {
                        OAuthServerSettings.OAuth2ServerSettings(
                            name = oauth.name,
                            authorizeUrl = oauth.authorizeUrl,
                            accessTokenUrl = oauth.accessTokenUrl,
                            requestMethod = HttpMethod.Post,
                            clientId = oauth.clientId,
                            clientSecret = oauth.clientSecret,
                            defaultScopes = oauth.defaultScopes,
                        )
                    }
                    client = HttpClient(CIO)
                }
            }

            server += KtorRouteConfig {
                authenticate(configName) {
                    get("/$path/login") { /* auto redirect to authorizeUrl */}
                    get("/$path/callback") {
                        val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

                    }
                }
            }

        }

    }


}