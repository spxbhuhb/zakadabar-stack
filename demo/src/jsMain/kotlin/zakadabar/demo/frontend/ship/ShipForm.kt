/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.builtin.form.ValidatedForm

class ShipForm(dto: ShipDto, mode: Mode) : ValidatedForm<ShipDto>(dto, Ships, mode) {

    override fun init() = build {
        + header("Ships")
        + section("Basics", "Data that all ships have.") {
            + dto::id
            + dto::name
            + select(dto::speed) {
                SpeedDto.all()
                    .map { it.id to it.description }
                    .sortedBy { it.second }
            }
        }
        + buttons()
    }

}