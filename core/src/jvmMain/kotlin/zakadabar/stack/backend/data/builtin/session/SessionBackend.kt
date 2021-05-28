/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.*
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.ktor.session.StackSession
import zakadabar.stack.backend.module
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.data.builtin.account.LogoutAction
import zakadabar.stack.data.builtin.account.SessionBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

/**
 * Session read (user account, roles), login and logout.
 */
object SessionBackend : EntityBackend<SessionBo>() {

    private val serverDescription by setting<ServerDescriptionBo>("zakadabar.server.description")

    override val boClass = SessionBo::class

    override var logActions = false // do not log passwords

    val accountBl by module<AccountBlProvider>()

    override fun onModuleLoad() {
        Sql.tables +=  SessionTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.action(LoginAction::class, ::action)
        route.action(LogoutAction::class, ::action)
    }

    /**
     * Read session data. Does not use entityId id as session is identified by Ktor.
     */
    override fun read(call: ApplicationCall, executor: Executor, entityId: EntityId<SessionBo>) = transaction {
        val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

        val anonymous = accountBl.anonymous()

        if (session.account == anonymous.id) {
            SessionBo(
                id = EntityId(),
                account = anonymous,
                anonymous = true,
                roles = emptyList(),
                serverDescription = serverDescription
            )
        } else {
            val account = accountBl.readPublic(session.account)
            val roles = accountBl.roles(account.id)
            SessionBo(
                id = EntityId(),
                account = account,
                anonymous = false,
                roles = roles.map { it.second },
                serverDescription = serverDescription
            )
        }
    }

    private fun action(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatusBo {

        val account = try {
            authenticate(executor.accountId, action.accountName, action.password) ?: return ActionStatusBo(false)
        } catch (ex: AccountLockedException) {
            return ActionStatusBo(false, "locked")
        }

        val roleIds = mutableListOf<EntityId<out BaseBo>>()
        val roleNames = mutableListOf<String>()

        accountBl.roles(account.id).forEach {
            roleIds += it.first
            roleNames += it.second
        }

        if (StackRoles.siteMember !in roleNames) return ActionStatusBo(false)

        call.sessions.set(StackSession(account.id, accountBl.anonymous().id == account.id, roleIds, roleNames))

        return ActionStatusBo(true)
    }

    private fun authenticate(executorAccountId: EntityId<out BaseBo>, accountName: String, password: Secret): AccountPublicBo? {
        val account = try {

            accountBl.authenticate(accountName, password)

        } catch (ex: InvalidCredentials) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: NoSuchElementException) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: AccountLockedException) {
            logger.warn("${executorAccountId}: /login result=locked name=${accountName}")
            throw ex
        } catch (ex: AccountExpiredException) {
            logger.warn("${executorAccountId}: /login result=expired name=${accountName}")
            return null
        } catch (ex: AccountNotValidatedException) {
            logger.warn("${executorAccountId}: /login result=not-validated name=${accountName}")
            return null
        } catch (ex: Exception) {
            logger.error("${executorAccountId}: /login result=error name=${accountName}", ex)
            throw ex
        }

        logger.info("${executorAccountId}: /login result=success new-account=${account.id} name=${accountName}")
        return account
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun action(call: ApplicationCall, executor: Executor, action: LogoutAction): ActionStatusBo {

        val old = call.sessions.get<StackSession>() !!

        val anonymous = accountBl.anonymous()

        logger.info("${executor.accountId}: /logout old=${old.account} new=${anonymous.id}")

        call.sessions.set(StackSession(anonymous.id, true, emptyList(), emptyList()))

        return ActionStatusBo(success = true)

    }
}