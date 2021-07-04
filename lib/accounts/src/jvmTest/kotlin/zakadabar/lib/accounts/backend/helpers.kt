/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import io.ktor.client.features.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.accounts.backend.bl.AccountPrivateBl
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.CreateAccount
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.stack.backend.server
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Creates a new account and executes a [LoginAction] with it.
 *
 * @return  BO of the account.
 */
suspend fun loginWithNew(): AccountPrivateBo {
    val (accountName, password, account) = createAccount()
    LoginAction(accountName, password).execute()
    return account
}

/**
 * Executes a [LogoutAction] and then runs the function.
 *
 * @param  func  The function to run as anonymous.
 */
suspend inline fun <reified T> withAnonymous(func: () -> T): T {
    LogoutAction().execute()
    return func()
}

/**
 * Executes a [LoginAction] with the given credentials and then runs the function.
 *
 * @param  accountName   Account name for login.
 * @param  password      Password for login.
 * @param  func          The function to run as anonymous.
 */
suspend inline fun <reified T> withLogin(accountName: String, password: Secret, func: () -> T): T {
    LoginAction(accountName, password).execute()
    val result = func()
    LogoutAction().execute()
    return result
}

/**
 * Executes a [LoginAction] with the `so`/`so` then runs the function.
 *
 * @param  func          The function to run as anonymous.
 */
suspend inline fun <reified T> withSo(func: () -> T): T {
    LoginAction("so", Secret("so")).execute()
    val result = func()
    LogoutAction().execute()
    return result
}

/**
 * Creates a new account with a UUID as account name and another
 * UUID as password.
 *
 * @param   useSo   When true the account is be created with `so`. When
 *                  false, the current login is used.
 * @return  Triple of accountName, password, account private BO.
 */
suspend fun createAccount(useSo : Boolean = true): Triple<String, Secret, AccountPrivateBo> {
    val accountName = UUID().toString()
    val password = Secret(UUID().toString())

    val create = CreateAccount(
        locked = false,
        validated = true,
        credentials = password,
        accountName = accountName,
        fullName = accountName,
        email = "a@b.c",
        phone = "12345678912",
        theme = null,
        locale = "en",
        roles = emptyList()
    )

    if (useSo) {
        withSo {
            assertTrue(create.execute().success)
        }
    } else {
        assertTrue(create.execute().success)
    }

    val bo = transaction {
        val accountBl = server.first<AccountPrivateBl>()
        accountBl.byName(accountName)
    }

    return Triple(accountName, password, bo)
}

/**
 * Logout, then execute an action as anonymous and assert that this returns with
 * HTTP status code 403 Forbidden.
 *
 * @param  func          The function to run as anonymous.
 */
suspend fun assertForbiddenForAnonymous(func: suspend () -> Unit): ClientRequestException {
    LogoutAction().execute()
    return assertForbidden(func)
}

/**
 * Create a new user, log in with that user and execute the action assert that this
 * returns with HTTP status code 403 Forbidden.
 *
 * @param  func          The function to run as a new user.
 */
suspend fun assertForbiddenForNew(func: suspend () -> Unit): ClientRequestException {
    loginWithNew()
    return assertForbidden(func)
}

/**
 * Execute an function with the current login and assert that this returns with
 * HTTP status code 403 Forbidden.
 *
 * @param  func          The function to run as the current user.
 */
suspend fun assertForbidden(func: suspend () -> Unit) =
    assertStatus(HttpStatusCode.Forbidden, func)

/**
 * Execute an action with the current login and assert that this returns
 * HTTP status code 401 Unauthorized.
 *
 * @param  func          The function to run.
 */
suspend fun assertUnauthorized(func: suspend () -> Unit) =
    assertStatus(HttpStatusCode.Unauthorized, func)

/**
 * Execute an action with the current login and assert that this returns
 * with the given status.
 *
 * @param  httpStatus    Status code to assert.
 * @param  func          The function to run.
 */
suspend fun assertStatus(httpStatus: HttpStatusCode, func: suspend () -> Unit): ClientRequestException {
    return assertFailsWith<ClientRequestException> {
        func()
    }.also {
        assertEquals(httpStatus, it.response.status)
    }
}