/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.business

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.sessions.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.authorize.AccountPublicBoV2
import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.*
import zakadabar.core.exception.Forbidden
import zakadabar.core.exception.Unauthorized
import zakadabar.core.exception.UnauthorizedData
import zakadabar.core.module.module
import zakadabar.core.persistence.EmptyPersistenceApi
import zakadabar.core.persistence.exposed.Sql
import zakadabar.core.server.ktor.KtorEntityRouter
import zakadabar.core.server.ktor.KtorSessionProvider
import zakadabar.core.server.ktor.executor
import zakadabar.core.server.server
import zakadabar.core.setting.setting
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.data.ModuleSettings
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.lib.accounts.server.ktor.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

class KtorSessionBl : EntityBusinessLogicBase<SessionBo>(
    boClass = SessionBo::class
), KtorSessionProvider {

    override val pa = EmptyPersistenceApi<SessionBo>()

    private val settings by setting<ModuleSettings>()

    override val authorizer = object : BusinessLogicAuthorizer<SessionBo> {
        override fun authorizeRead(executor: Executor, entityId: EntityId<SessionBo>) {
            // everyone can read their own session
        }

        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            when (actionBo) {
                is LoginAction -> return
                is LogoutAction -> return
                else -> throw Forbidden()
            }
        }
    }

    override val auditor = auditor {
        includeData = false
    }

    override val router = object : KtorEntityRouter<SessionBo>(this) {

        init {
            action(LoginAction::class, ::loginAction)
            action(LogoutAction::class, ::logoutAction)
        }

        override suspend fun read(call: ApplicationCall, id: String) {
            apiCacheControl(call)
            call.respond(read(call) as Any)
        }

        override suspend fun action(call: ApplicationCall, actionClass: KClass<out BaseBo>, actionFunc: (Executor, BaseBo) -> Any?) {
            val executor = call.executor()
            val aText = call.receive<String>()
            val aObj = Json.decodeFromString(serializer(actionClass.createType()), aText) as BaseBo

            val response = transaction {
                when (aObj) {
                    is LoginAction -> loginAction(call, executor, aObj)
                    is LogoutAction -> logoutAction(call, executor)
                    else -> throw NotImplementedError()
                }
            }

            @Suppress("UNCHECKED_CAST")
            call.respond(response as Any)
        }

    }

    private val accountBl by module<AccountBlProvider>()

    override fun onModuleLoad() {
        Sql.tables += SessionTable
    }

    fun read(call: ApplicationCall) = transaction {
        val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

        val anonymous = accountBl.anonymous()

        val serverDescription = server.description

        if (session.account == anonymous.accountId) {
            SessionBo(
                id = EntityId("current"),
                account = anonymous,
                anonymous = true,
                roles = emptyList(),
                permissions = emptyList(),
                serverDescription = serverDescription
            )
        } else {
            val account = accountBl.readPublic(session.account)
            val roles = accountBl.roles(account.accountId)
            val permissions = accountBl.permissions(account.accountId)
            SessionBo(
                id = EntityId("current"),
                account = account,
                anonymous = false,
                roles = roles.map { it.second },
                permissions = permissions.map { it.second },
                serverDescription = serverDescription
            )
        }
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun loginAction(executor: Executor, action: LoginAction): ActionStatus {
        throw IllegalStateException("reached placeholder action")
    }

    private fun loginAction(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatus {

        val session = login(executor, action)

        if (settings.loginActionRole.isNotEmpty() && settings.loginActionRole !in session.roleNames) {
            throw Unauthorized(data = UnauthorizedData(missingRole = true))
        }

        call.sessions.set(session)

        return ActionStatus(true)
    }

    fun login(executor: Executor, action: LoginAction): StackSession {

        val account = authenticate(executor, action.accountName, action.password)

        val roleIds = mutableListOf<EntityId<out BaseBo>>()
        val roleNames = mutableListOf<String>()
        val permissionIds = mutableListOf<EntityId<out BaseBo>>()
        val permissionNames = mutableListOf<String>()

        accountBl.roles(account.accountId).forEach {
            roleIds += it.first
            roleNames += it.second
        }
        accountBl.permissions(account.accountId).forEach {
            permissionIds += it.first
            permissionNames += it.second
        }

        return StackSession(
            account.accountId, account.accountUuid, accountBl.anonymous().accountId == account.accountId,
            roleIds, roleNames,
            permissionIds, permissionNames
        )
    }

    private fun authenticate(executor: Executor, accountName: String, password: Secret): AccountPublicBoV2 =
        accountBl.authenticateV2(executor, accountName, password)

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun logoutAction(executor: Executor, action: LogoutAction): ActionStatus {
        throw IllegalStateException("reached placeholder action")
    }

    private fun logoutAction(call: ApplicationCall, executor: Executor): ActionStatus {

        val old = call.sessions.get<StackSession>() !!

        val anonymous = accountBl.anonymousV2()

        auditor.auditCustom(executor) { "logout accountId=${old.account}" }

        call.sessions.set(StackSession(anonymous.accountId, anonymous.accountUuid, true, emptyList(), emptyList(), emptyList(), emptyList()))

        return ActionStatus(success = true)

    }

    override fun configure(conf: Sessions.Configuration) {
        with(conf) {
            val sessionType = StackSession::class

            val name = with(server.settings.ktor) {
                if (portCookie) "ZKL_SESSION_$port" else "ZKL_SESSION"
            }

            @Suppress("DEPRECATION") // as in Ktor code
            val builder = CookieIdSessionBuilder(sessionType).apply {
                cookie.path = "/"
            }
            val transport = SessionTransportCookie(name, builder.cookie, builder.transformers)
            val tracker = RenewableSessionTrackerById(sessionType, StackSessionSerializer, SessionStorageSql, builder.sessionIdProvider)
            val provider = SessionProvider(name, sessionType, transport, tracker)
            register(provider)

            SessionMaintenanceTask.start()
        }
    }

    override fun configure(conf: Authentication.Configuration) {
        conf.configureSession()
    }

}