/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.samples.theplace.ThePlace
import zakadabar.samples.theplace.data.ShipDto
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.comm.rest.FrontendComm
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew
import zakadabar.stack.frontend.util.launch

object Ships : ZkCrud(ThePlace.shid, "/ships") {

    override fun all() = launchBuildNew {
        val ships = ShipDto.all()
        ships.forEach { + it.name }
    }

    override fun create() = buildNew {
        + ShipForm(ShipDto(0, "", ""))
    }

    override fun read(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId))

        + SimpleButton("back") { Application.back() }
        + SimpleButton("edit") { openUpdate(recordId) }

    }

    override fun update(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId))

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") { zkElement[ShipForm::class].submit() }

    }

    override fun delete(recordId: Long) = launchBuildNew {

        val dto = ShipDto.read(recordId)

        + ShipForm(dto)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") {
            launch {
                dto.delete()
                Application.back()
            }
        }

    }

    class ShipForm(private val dto: ShipDto) : ZkElement() {
        override fun init() = build {
            + dto.name
        }

        fun submit() {

        }
    }

}

