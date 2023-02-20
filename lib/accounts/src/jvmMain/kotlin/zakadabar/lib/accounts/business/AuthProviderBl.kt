/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.business

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.EntityId
import zakadabar.core.data.Secret
import zakadabar.core.data.QueryBo
import zakadabar.core.exception.Unauthorized
import zakadabar.core.module.module
import zakadabar.core.server.ktor.plusAssign
import zakadabar.core.setting.setting
import zakadabar.core.server.server
import zakadabar.lib.accounts.data.*
import zakadabar.lib.accounts.jwt.*
import zakadabar.lib.accounts.server.ktor.Oauth2
import zakadabar.lib.accounts.server.ktor.Oauth2.Companion.login
import zakadabar.lib.accounts.server.ktor.StackSession
import kotlin.NoSuchElementException
import kotlin.random.Random

class AuthProviderBl : BusinessLogicCommon<AuthProviderBo>() {

    override val namespace: String = AuthProviderBo.boNamespace

    private val settings by setting<ModuleSettings>()

    private val accountBl by module<AccountPrivateBl>()

    override val authorizer = object : BusinessLogicAuthorizer<AuthProviderBo> {
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            // everyone can query auth providers
        }
    }

    override val router = router {
        query(AuthProviderList::class, ::authProviderList)
    }

    var callback : ((Executor, Oauth2.Token) -> Unit)? = null

    override fun onModuleStart() {
        super.onModuleStart()

        settings.oauth?.forEach { oauth->

            logger.info("Register Oauth: ${oauth.name} - ${oauth.displayName}")

            val handler = Oauth2(oauth, ::callback)

            server += handler.authConfig
            server += handler.routeConfig

        }

    }

    private fun callback(token: Oauth2.Token) : StackSession {

        val oauth = token.authSettings
        val idToken = token.idToken

        val accountName = idToken.getString(oauth.claims.accountName) ?: throw Unauthorized("missing claim: ${oauth.claims.accountName}")

        //logger.debug("Incoming account: $accountName")
        //logger.debug("Claims: ${idToken.claims}")

        var executor = try {
           accountBl.executorFor(accountName)
        } catch (ex: NoSuchElementException) {
            if (oauth.autoRegister) register(token)
            else throw Unauthorized("Account not found: $accountName")
        }

        callback?.invoke(executor, token)

        executor = accountBl.executorFor(accountName)

        accountBl.pa.withTransaction {
            accountBl.authenticate(executor, EntityId(executor.accountId), "", false, oauth.name)
        }

        return executor.toSession()
    }

    private fun register(token: Oauth2.Token) : Executor {
        val oauth = token.authSettings
        val idToken = token.idToken
        val claims = oauth.claims

        val account = accountBl.pa.withTransaction {
            accountBl.createRecords(
                CreateAccount(
                    credentials = Secret.random(),
                    accountName = idToken.getString(claims.accountName)!!,
                    fullName = idToken.getString(claims.fullName)!!,
                    email = idToken.getString(claims.email) ?: "noreply@localhost",
                    phone = null,
                    theme = null,
                    locale = "",
                    validated = true,
                    locked = false,
                    roles = emptyList()
                )
            )
        }
        return accountBl.executorFor(account)
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

    private fun Secret.Companion.random() = Secret(String(Random.nextBytes(47), Charsets.ISO_8859_1))

}