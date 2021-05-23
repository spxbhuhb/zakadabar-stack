/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.ktor.session.StackSession
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.*
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

    override fun onModuleLoad() {
        + SessionTable
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

        if (session.account == Server.anonymous.id) {
            SessionBo(
                id = EntityId(),
                account = Server.anonymous,
                anonymous = true,
                roles = emptyList(),
                serverDescription = serverDescription
            )
        } else {
            val (account, principalId) = Server.findAccountById(session.account)
            val (_, roleNames) = PrincipalBackend.roles(principalId)
            SessionBo(
                id = EntityId(),
                account = account,
                anonymous = false,
                roles = roleNames,
                serverDescription = serverDescription
            )
        }
    }

    private fun action(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatusBo {

        val result = try {
            authenticate(executor.accountId, action.accountName, action.password.value, throwLocked = true) ?: return ActionStatusBo(false)
        } catch (ex: PrincipalBackend.AccountLockedException) {
            return ActionStatusBo(false, "locked")
        }

        val (account, principalId) = result

        val (roleIds, roleNames) = PrincipalBackend.roles(principalId)

        if (StackRoles.siteMember !in roleNames) return ActionStatusBo(false)

        call.sessions.set(StackSession(EntityId(account.id.toLong()), roleIds, roleNames))

        return ActionStatusBo(true)
    }

    fun authenticate(executorAccountId: EntityId<out BaseBo>, accountName: String, password: String, throwLocked: Boolean = false): Pair<AccountPublicBo, EntityId<PrincipalBo>>? {
        val (account, principalId) = try {

            val (account, principalId) = Server.findAccountByName(accountName)

            PrincipalBackend.authenticate(principalId, password)

            account to principalId

        } catch (ex: PrincipalBackend.InvalidCredentials) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: NoSuchElementException) {
            logger.warn("${executorAccountId}: /login result=fail name=${accountName}")
            return null
        } catch (ex: PrincipalBackend.AccountLockedException) {
            logger.warn("${executorAccountId}: /login result=locked name=${accountName}")
            if (throwLocked) throw ex
            return null
        } catch (ex: PrincipalBackend.AccountExpiredException) {
            logger.warn("${executorAccountId}: /login result=expired name=${accountName}")
            return null
        } catch (ex: PrincipalBackend.AccountNotValidatedException) {
            logger.warn("${executorAccountId}: /login result=not-validated name=${accountName}")
            return null
        } catch (ex: Exception) {
            logger.error("${executorAccountId}: /login result=error name=${accountName}", ex)
            throw ex
        }

        logger.info("${executorAccountId}: /login result=success new-account=${account.id} name=${accountName}")
        return account to principalId
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun action(call: ApplicationCall, executor: Executor, action: LogoutAction): ActionStatusBo {

        val old = call.sessions.get<StackSession>() !!

        logger.info("${executor.accountId}: /logout old=${old.account} new=${Server.anonymous.id}")

        call.sessions.set(StackSession(EntityId(Server.anonymous.id.toLong()), emptyList(), emptyList()))

        return ActionStatusBo(success = true)
    }
}