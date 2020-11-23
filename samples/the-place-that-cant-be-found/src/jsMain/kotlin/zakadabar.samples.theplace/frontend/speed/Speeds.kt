/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend.speed

import zakadabar.samples.theplace.ThePlace
import zakadabar.samples.theplace.data.SpeedDto
import zakadabar.samples.theplace.frontend.form.ValidatedForm
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew

object Speeds : ZkCrud(ThePlace.shid, "/speeds") {

    override fun all() = launchBuildNew {
        + SimpleButton("new") { Speeds.openCreate() }
        SpeedDto.all().forEach { + it.description }
    }

    override fun create() = buildNew {
        + "New Speed"
        + SpeedForm(SpeedDto(0, "", 0.0), ValidatedForm.Mode.Create)
        + SimpleButton("back") { Application.back() }
        + SimpleButton("save") { zkElement[SpeedForm::class].submit() }
    }

    override fun read(recordId: Long) = launchBuildNew {

        + SpeedForm(SpeedDto.read(recordId), ValidatedForm.Mode.Read)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("edit") { openUpdate(recordId) }

    }

    override fun update(recordId: Long) = launchBuildNew {

        + SpeedForm(SpeedDto.read(recordId), ValidatedForm.Mode.Update)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") { zkElement[SpeedForm::class].submit() }

    }

    override fun delete(recordId: Long) = launchBuildNew {

        + SpeedForm(SpeedDto.read(recordId), ValidatedForm.Mode.Delete)

        + SimpleButton("back") { Application.back() }
        + SimpleButton("submit") { zkElement[SpeedForm::class].submit() }

    }

}

