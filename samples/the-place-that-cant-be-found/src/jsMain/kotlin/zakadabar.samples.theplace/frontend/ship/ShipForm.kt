/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend.ship

import zakadabar.samples.theplace.data.ShipDto
import zakadabar.samples.theplace.frontend.form.ValidatedForm

class ShipForm(dto: ShipDto, mode: Mode) : ValidatedForm<ShipDto>(dto, mode) {

    override fun init(): ValidatedForm<ShipDto> {

        this build {
            + dto::id
            + dto::name
            + dto::speed
        }

        return this
    }

}