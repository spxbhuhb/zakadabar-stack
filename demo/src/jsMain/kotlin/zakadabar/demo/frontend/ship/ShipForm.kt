/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.frontend.form.ValidatedForm

class ShipForm(dto: ShipDto, mode: Mode) : ValidatedForm<ShipDto>(dto, Ships, mode) {

    override fun init() = build {
        + header("Ships")
        + section("Basics", "Data that all ships have.") {
            + dto::id
            + dto::name
            + dto::speed
        }
        + buttons()
    }

}