/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import io.ktor.client.features.*
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.data.builtin.ExampleEnum
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.core.util.default
import zakadabar.core.data.CommBase
import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.entity.EntityId

suspend fun main() {
    CommBase.baseUrl = "http://localhost:8080"
    crud()
    login()
    errorHandling()
}

suspend fun crud() {
    println("======== CRUD ========")
    dumpBuiltins("before create")

    // with the constructor we have to initialize all fields

    val newReference = ExampleReferenceBo(
        EntityId(), // we don't have an id yet
        name = "hello world"
    ).create()

    // with default all fields are initialized with the default values from the schema

    val newBuiltin = BuiltinBo.default {
        enumSelectValue = ExampleEnum.EnumValue1
        secretValue = Secret("aaa")
        recordSelectValue = newReference.id
        stringValue = "hello"
        stringSelectValue = "something"
        textAreaValue = "something"
    }.create()

    dumpBuiltins("after create")

    val builtinRead = BuiltinBo.read(newBuiltin.id)
    builtinRead.doubleValue = 5.6
    builtinRead.update()

    builtinRead.delete()
    newReference.delete()

    dumpBuiltins("after delete")
}

suspend fun dumpBuiltins(message: String) {
    println("\n    ---- $message ----\n")
    BuiltinBo.all().forEach { println("        $it") }
}

suspend fun login() {
    println("\n======== Login ========\n")

    var session = SessionBo.read(EntityId("current"))

    println("    ---- at start ----\n")
    println("        $session\n")

    var actionStatus = LoginAction("demo", Secret("wrong")).execute()
    session = SessionBo.read(EntityId("current"))

    println("    ---- unsuccessful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    actionStatus = LoginAction("demo", Secret("demo")).execute()
    session = SessionBo.read(EntityId("current"))

    println("    ---- successful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    println("    ---- after successful login ----\n")

    val account = AccountPrivateBo.read(EntityId(session.account.accountId))
    println("        $account\n")

    actionStatus = LogoutAction().execute()

    println("    ---- logout ----\n")
    println("        $actionStatus\n")

    try {
        AccountPrivateBo.read(EntityId(session.account.accountId))
    } catch (ex: ClientRequestException) {
        println("    ---- after logout ----\n")
        println("        ${ex.response}")
    }
}

suspend fun errorHandling() {
    println("\n======== Error Handling ========\n")
    try {
        BuiltinBo.read(EntityId(- 1))
    } catch (ex: ServerResponseException) {
        println("    ${ex.response.status}")
    } catch (ex: ClientRequestException) {
        println("    ${ex.response}")
    }

    println()

    CommBase.onError = { ex ->
        when (ex) {
            is ServerResponseException -> println("    onError:    server exception: ${ex.response.status}")
            is ClientRequestException -> println("    onError:    client exception: ${ex.response.status}")
            else -> println("    onError:    general exception: $ex")
        }
    }

    try {
        BuiltinBo.read(EntityId(- 1))
    } catch (ex: Exception) {
        // don't do anything here, the global error handler handled the exception
    }
}
