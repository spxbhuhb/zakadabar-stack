/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.business

import com.auth0.jwt.interfaces.DecodedJWT
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.authorize.AccountPublicBoV2
import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.exception.Unauthorized
import zakadabar.core.module.module
import zakadabar.core.server.ktor.plusAssign
import zakadabar.core.setting.setting
import zakadabar.core.server.server
import zakadabar.core.util.toStackUuid
import zakadabar.lib.accounts.data.*
import zakadabar.lib.accounts.server.ktor.Oauth2
import zakadabar.lib.accounts.server.ktor.Oauth2.Companion.login
import zakadabar.lib.accounts.server.ktor.StackSession
import java.util.*

class AuthProviderBl() : BusinessLogicCommon<AuthProviderBo>() {

    override val namespace: String = AuthProviderBo.boNamespace

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

    companion object {
        const val JWT_CLAIM_EMAIL = "email"
        const val JWT_CLAIM_ROLES = "roles"
        const val JWT_CLAIM_NAME = "name"
    }
    override fun onModuleStart() {
        super.onModuleStart()

        settings.oauth?.forEach { oauth->

            println("Register Oauth: ${oauth.name} - ${oauth.displayName}")

            val handler = Oauth2(oauth, ::callback)

            server += handler.authConfig
            server += handler.routeConfig

        }

    }

    private fun callback(oauth: OauthSettings, jwt: DecodedJWT) : StackSession {
        val account = jwt.toAccount()

        val executor : Executor = try {
            accountBl.executorFor(account.accountName)
        } catch(ex: Exception) {
            if (oauth.autoRegister) register(account)
            else throw Unauthorized("account not found: $account.accountName")
        }

        val roles = jwt.roles

        syncRoles(executor.accountId, roles)

        return executor.toSession()
    }

    private fun register(account: AccountPublicBoV2) : Executor {
        TODO("not implemented yet")
    }

    private fun syncRoles(accountId:  EntityId<out BaseBo>, roles: Set<String>) {

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

    private fun DecodedJWT.toAccount() = AccountPublicBoV2(
        accountId = EntityId(),
        accountUuid = UUID.randomUUID().toStackUuid(),
        accountName = getClaim(JWT_CLAIM_EMAIL).asString(),
        fullName = getClaim(JWT_CLAIM_NAME).asString(),
        email = getClaim(JWT_CLAIM_EMAIL).asString(),
        phone = null,
        theme = null,
        locale = ""
    )
    private val DecodedJWT.roles : Set<String> get() {
        val roles = getClaim(JWT_CLAIM_ROLES).asList(String::class.java)
        return roles.map {
            //TODO: role mapping!
            it
        }.toSet()
    }


}