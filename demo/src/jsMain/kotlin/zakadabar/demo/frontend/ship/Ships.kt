/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.ship

import zakadabar.demo.Demo
import zakadabar.demo.data.ShipDto
import zakadabar.demo.frontend.form.ValidatedForm
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew
import zakadabar.stack.frontend.util.launch

object Ships : ZkCrud(Demo.shid, "/ships") {

    override fun all() = launchBuildNew {
        val ships = ShipDto.all()
        + SimpleButton("new") { Ships.openCreate() }
        ships.forEach { + it.name }
    }

    override fun create() = buildNew {
        + "New Ship"
        + ShipForm(ShipDto(0, "", 0), ValidatedForm.Mode.Create)
    }

    override fun read(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId), ValidatedForm.Mode.Read)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("edit") { openUpdate(recordId) }

    }

    override fun update(recordId: Long) = launchBuildNew {

        + ShipForm(ShipDto.read(recordId), ValidatedForm.Mode.Update)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") { zkElement[ShipForm::class].submit() }

    }

    override fun delete(recordId: Long) = launchBuildNew {

        val dto = ShipDto.read(recordId)

        + ShipForm(dto, ValidatedForm.Mode.Delete)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") {
            launch {
                dto.delete()
                Application.back()
            }
        }

    }
}

