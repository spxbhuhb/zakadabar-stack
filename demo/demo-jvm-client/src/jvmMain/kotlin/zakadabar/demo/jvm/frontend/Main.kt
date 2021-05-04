/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.jvm.frontend

import io.ktor.client.features.*
import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.ship.ShipDto
import zakadabar.demo.marina.data.speed.SpeedDto
import zakadabar.stack.backend.util.default
import zakadabar.stack.data.builtin.account.*
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordComm

suspend fun main() {
    RecordComm.baseUrl = "http://localhost:8080"
    crud()
    login()
    errorHandling()
}

suspend fun crud() {
    println("======== CRUD ========")
    dumpShips("before create")

    val speedDto = SpeedDto.all().first()
    val portDto = PortDto.all().first()
    val captainDto = AccountPublicDto.all().first()

    // with the constructor we have to initialize all fields

    val newShip = ShipDto(
        EmptyRecordId(), // we don't have an ide yet
        name = "Boat",
        speed = speedDto.id,
        captain = captainDto.id,
        description = "This ship is built by a standalone JVM client.",
        hasPirateFlag = true,
        port = portDto.id
    ).create()

    // with default all fields are initialized with the default values from the schema

    val newShip2 = ShipDto.default {
        name = "Boat"
        this.speed = speedDto.id
        this.captain = captainDto.id
    }.create()

    dumpShips("after create")

    val boat = ShipDto.read(newShip.id)
    boat.hasPirateFlag = false
    boat.update()

    boat.delete()
    newShip2.delete()

    dumpShips("after delete")
}

suspend fun dumpShips(message: String) {
    println("\n    ---- $message ----\n")
    ShipDto.all().forEach { println("        $it") }
}

suspend fun login() {
    println("\n======== Login ========\n")

    var session = SessionDto.read(EmptyRecordId())

    println("    ---- at start ----\n")
    println("        $session\n")

    var actionStatus = LoginAction("demo", Secret("wrong")).execute()
    session = SessionDto.read(EmptyRecordId())

    println("    ---- unsuccessful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    actionStatus = LoginAction("demo", Secret("demo")).execute()
    session = SessionDto.read(EmptyRecordId())

    println("    ---- successful login ----\n")
    println("        $actionStatus\n")
    println("        $session\n")

    println("    ---- after successful login ----\n")

    val account = AccountPrivateDto.read(LongRecordId(session.account.id.toLong()))
    println("        $account\n")

    actionStatus = LogoutAction().execute()

    println("    ---- logout ----\n")
    println("        $actionStatus\n")

    try {
        AccountPrivateDto.read(LongRecordId(session.account.id.toLong()))
    } catch (ex: ClientRequestException) {
        println("    ---- after logout ----\n")
        println("        ${ex.response}")
    }
}

suspend fun errorHandling() {
    println("\n======== Error Handling ========\n")
    try {
        ShipDto.read(LongRecordId(- 1))
    } catch (ex: ClientRequestException) {
        println("    ${ex.response}")
    }
}
