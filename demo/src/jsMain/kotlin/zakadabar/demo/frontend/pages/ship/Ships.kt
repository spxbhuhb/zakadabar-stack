/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.simple.SimpleButton
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement.Companion.buildNew
import zakadabar.stack.frontend.elements.ZkElement.Companion.launchBuildNew

object Ships : ZkCrud<ShipDto>() {

    override fun all() = launchBuildNew {
        + SimpleButton(Strings.new) { Ships.openCreate() }
        + ShipTable().setData(ShipDto.all().sortedBy { it.id })
    }

    override fun create() = buildNew {
        + ShipForm(ShipDto(0, "", 0), FormMode.Create)
    }

    override fun read(recordId: Long) = launchBuildNew {
        + ShipForm(ShipDto.read(recordId), FormMode.Read)
    }

    override fun update(recordId: Long) = launchBuildNew {
        + ShipForm(ShipDto.read(recordId), FormMode.Update)
    }

    override fun delete(recordId: Long) = launchBuildNew {
        + ShipForm(ShipDto.read(recordId), FormMode.Delete)
    }

}

