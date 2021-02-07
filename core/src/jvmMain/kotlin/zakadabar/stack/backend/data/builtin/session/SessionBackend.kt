/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.builtin.session

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.LoginAction
import zakadabar.stack.data.builtin.LogoutAction
import zakadabar.stack.data.builtin.SessionDto
import zakadabar.stack.util.Executor

/**
 * Session read (user account, roles), login and logout.
 */
object SessionBackend : RecordBackend<SessionDto>() {

    override val dtoClass = SessionDto::class

    override fun onModuleLoad() {
        + SessionTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.action(LoginAction::class, ::action)
        route.action(LogoutAction::class, ::action)
    }

    /**
     * Read session data. Does not use record id as session is identified by Ktor.
     */
    override fun read(call: ApplicationCall, executor: Executor, recordId: Long) = transaction {
        val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

        if (session.account == Server.anonymous.id) {
            SessionDto(
                id = 1,
                account = Server.anonymous,
                anonymous = true,
                roles = emptyList()
            )
        } else {
            val (account, principalId) = Server.findAccountById(session.account)
            SessionDto(
                id = 1,
                account = account,
                anonymous = false,
                roles = PrincipalBackend.roles(principalId)
            )
        }
    }

    private fun action(call: ApplicationCall, executor: Executor, action: LoginAction): ActionStatusDto {
        val old = call.sessions.get<StackSession>() !!

        val account = try {

            val (account, principalId) = Server.findAccountByName(action.accountName)

            PrincipalBackend.authenticate(principalId, action.password)

            account

        } catch (ex: NoSuchElementException) {
            logger.warn("${executor.accountId}: /login result=fail current-account=${old.account} name=${action.accountName}")
            return ActionStatusDto(success = false)
        } catch (ex: PrincipalBackend.AccountLockedException) {
            logger.warn("${executor.accountId}: /login result=locked current-account=${old.account} name=${action.accountName}")
            return ActionStatusDto(success = false)
        } catch (ex: PrincipalBackend.AccountExpiredException) {
            logger.warn("${executor.accountId}: /login result=expired current-account=${old.account} name=${action.accountName}")
            return ActionStatusDto(success = false)
        } catch (ex: PrincipalBackend.AccountNotValidatedException) {
            logger.warn("${executor.accountId}: /login result=not-validated current-account=${old.account} name=${action.accountName}")
            return ActionStatusDto(success = false)
        } catch (ex: Exception) {
            logger.error("${executor.accountId}: /login result=error current-account=${old.account} name=${action.accountName}", ex)
            throw ex
        }

        logger.info("${executor.accountId}: /login result=success current-account=${old.account} new-account=${account.id} name=${action.accountName}")

        call.sessions.set(StackSession(account.id))

        return ActionStatusDto(success = true)
    }

    @Suppress("UNUSED_PARAMETER") // action is needed here because of route mapping
    private fun action(call: ApplicationCall, executor: Executor, action: LogoutAction): ActionStatusDto {

        val old = call.sessions.get<StackSession>() !!

        logger.info("${executor.accountId}: /logout old=${old.account} new=${Server.anonymous.id}")

        call.sessions.set(StackSession(Server.anonymous.id))

        return ActionStatusDto(success = true)
    }
}