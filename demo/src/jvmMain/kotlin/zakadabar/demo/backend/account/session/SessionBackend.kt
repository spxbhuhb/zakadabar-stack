/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.account.session

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.account.account.AccountPrivateDao
import zakadabar.demo.backend.account.account.AccountPrivateTable
import zakadabar.demo.data.account.AccountPublicDto
import zakadabar.demo.data.account.SessionDto
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.custom.CustomBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.session.StackSession
import zakadabar.stack.backend.util.executor
import zakadabar.stack.data.builtin.LoginDto

object SessionBackend : CustomBackend() {

    override fun install(route: Route) {
        with(route) {
            route(SessionDto.recordType) {

                // the frontend calls
                get("0") {
                    val session = call.sessions.get<StackSession>() ?: throw IllegalStateException()

                    val dto = if (session.account == Server.anonymous.id) {
                        SessionDto(
                            id = 0,
                            account = Server.anonymous as AccountPublicDto,
                            roles = emptyList()
                        )
                    } else {
                        transaction {

                            val account = AccountPrivateDao[session.account]

                            SessionDto(
                                id = 0,
                                account = account.toPublicDto(),
                                roles = PrincipalBackend.roles(account.principal.id.value)
                            )
                        }
                    }

                    call.respond(dto)
                }

            }

            patch("login") {
                val executor = call.executor()
                val request = call.receive(LoginDto::class)

                val old = call.sessions.get<StackSession>() !!

                val account = try {

                    val account = AccountPrivateDao
                        .find { AccountPrivateTable.accountName eq request.accountName }
                        .firstOrNull() ?: throw NoSuchElementException()

                    PrincipalBackend.authenticate(account.principal.id.value, request.password)

                    account

                } catch (ex: NoSuchElementException) {
                    logger.info("${executor.accountId}: /login result=fail session=${old.account} name=${request.accountName}")
                    call.respond(HttpStatusCode.NotFound)
                    return@patch
                } catch (ex: PrincipalBackend.AccountLockedException) {
                    logger.info("${executor.accountId}: /login result=locked session=${old.account} name=${request.accountName}")
                    call.respond(HttpStatusCode.Locked)
                    return@patch
                } catch (ex: PrincipalBackend.AccountExpiredException) {
                    logger.info("${executor.accountId}: /login result=expired session=${old.account} name=${request.accountName}")
                    call.respond(HttpStatusCode.Gone)
                    return@patch
                } catch (ex: PrincipalBackend.AccountNotValidatedException) {
                    logger.info("${executor.accountId}: /login result=not-validated session=${old.account} name=${request.accountName}")
                    call.respond(HttpStatusCode.Conflict)
                    return@patch
                } catch (ex: Exception) {
                    logger.error("${executor.accountId}: /login result=error session=${old.account} name=${request.accountName}", ex)
                    throw ex
                }

                logger.info("${executor.accountId}: /login result=success session=${old.account} new=${account.id} name=${request.accountName}")

                val new = StackSession(account.id.value)

                val dto = SessionDto(
                    id = 0,
                    account = account.toPublicDto(),
                    roles = PrincipalBackend.roles(account.id.value)
                )

                call.sessions.set(new)
                call.respond(dto)
            }

            patch("logout") {
                val executor = call.executor()

                val old = call.sessions.get<StackSession>() !!

                logger.info("${executor.accountId}: /logout old=${old.account} new=${Server.anonymous.id}")

                val new = StackSession(Server.anonymous.id)

                val dto = SessionDto(
                    id = 0,
                    account = Server.anonymous as AccountPublicDto,
                    roles = emptyList()
                )

                call.sessions.set(new)
                call.respond(dto)
            }

        }
    }
}