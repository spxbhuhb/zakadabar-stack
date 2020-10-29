/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.samples.theplace.data.ShipDto
import zakadabar.stack.frontend.application.navigation.Navigation
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.buildNew
import zakadabar.stack.frontend.elements.launchBuildNew
import zakadabar.stack.frontend.util.launch

class ShipForm(private val dto: ShipDto) : ZkElement() {
    override fun init() = build {
        + dto.name
    }

    fun submit() {

    }
}

fun ships() = launchBuildNew {
    val ships = ShipDto.all()
    ships.forEach { + it.name }
}

fun createShip() = buildNew {
    + ShipForm(ShipDto(0, "", ""))
}

fun readShip(recordId: Long) = launchBuildNew {

    + createShip()

    + ShipForm(ShipDto.read(recordId))

    + SimpleButton("back") { Navigation.back() }
    + SimpleButton("edit") { Navigation.changeLocation("") }

}

fun updateShip(recordId: Long) = launchBuildNew {

    + ShipForm(ShipDto.read(recordId))

    + SimpleButton("back") { Navigation.back() }
    + SimpleButton("submit") { zkElement[ShipForm::class].submit() }

}

fun deleteShip(recordId: Long) = launchBuildNew {

    val dto = ShipDto.read(recordId)

    + ShipForm(dto)

    + SimpleButton("back") { Navigation.back() }
    + SimpleButton("submit") {
        launch {
            dto.delete()
            Navigation.back()
        }
    }

}
