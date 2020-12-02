/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.builtin.form.ValidatedForm
import zakadabar.stack.frontend.builtin.form.fields.Images

class ShipForm(dto: ShipDto, mode: Mode) : ValidatedForm<ShipDto>(dto, Ships, mode) {

    override fun init() = build {
        + header("Ships")
        + column {
            + row {
                + section("Basics", "Data that all ships have.") {
                    + fieldGrid {
                        ifNotCreate {
                            + "id"
                            + dto::id
                        }
                        + "name"
                        + dto::name
                        + "speed"
                        + select(dto::speed) {
                            SpeedDto.all()
                                .map { it.id to it.description }
                                .sortedBy { it.second }
                        }
                    }
                } marginRight 8
                + section("Description") {
                    + textarea(dto::name)
                }
            }
            + section("Images") {
                + Images(this@ShipForm, dto.id)
            }
            + buttons()
        }
    }

}