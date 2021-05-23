/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import io.ktor.client.features.*
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.data.builtin.account.*
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordComm
import zakadabar.stack.data.record.StringRecordId

suspend fun main() {
    RecordComm.baseUrl = "http://localhost:8080"
    crud()
    login()
    errorHandling()
}

suspend fun crud() {
    println("======== CRUD ========")
    dumpBuiltins("before create")

    val builtin = BuiltinDto.all().first()
    val reference = ExampleReferenceDto.all().first()
    val account = AccountPublicBo.all().first()

    // with the constructor we have to initialize all fields

    val newReference = ExampleReferenceDto(
        EmptyRecordId(), // we don't have an id yet
        name = "hello world"
    ).create()

    // with default all fields are initialized with the default values from the schema

    val newBuiltin = BuiltinDto.default {
        recordSelectValue = newReference.id
    }.create()

    dumpBuiltins("after create")

    val builtinRead = BuiltinDto.read(newBuiltin.id)
    builtinRead.doubleValue = 5.6
    builtinRead.update()

    builtinRead.delete()
    newReference.delete()

    dumpBuiltins("after delete")
}

suspend fun dumpBuiltins(message: String) {
    println("\n    ---- $message ----\n")
    BuiltinDto.all().forEach { println("        $it") }
}

suspend fun login() {
    println("\n======== Login ========\n")

    var session = SessionBo.read(StringRecordId("own"))

    println("    ---- at start ----\n")
    println("        $session\n")

    var actionStatus = LoginAction("demo", Secret("wrong")).execute()
    session = SessionBo.read(StringRecordId("own"))

    println("    ---- unsuccessful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    actionStatus = LoginAction("demo", Secret("demo")).execute()
    session = SessionBo.read(StringRecordId("own"))

    println("    ---- successful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    println("    ---- after successful login ----\n")

    val account = AccountPrivateBo.read(LongRecordId(session.account.id.toLong()))
    println("        $account\n")

    actionStatus = LogoutAction().execute()

    println("    ---- logout ----\n")
    println("        $actionStatus\n")

    try {
        AccountPrivateBo.read(LongRecordId(session.account.id.toLong()))
    } catch (ex: ClientRequestException) {
        println("    ---- after logout ----\n")
        println("        ${ex.response}")
    }
}

suspend fun errorHandling() {
    println("\n======== Error Handling ========\n")
    try {
        BuiltinDto.read(LongRecordId(- 1))
    } catch (ex: ClientRequestException) {
        println("    ${ex.response}")
    }
}
