/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.Images
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses

class ShipForm : ZkForm<ShipDto>() {

    override fun init() = build {
        + header(Strings.Ship.ships)
        + column {
            + row {
                + basics() marginRight 8
                + description()
            }
            + images()
            + buttons()
        }
    }

    private fun basics() =
        section(Strings.basics, Strings.Ship.Basics.explanation) {
            + fieldGrid {
                ifNotCreate {
                    + Strings.id
                    + dto::id
                }
                + Strings.name
                + dto::name
                + Strings.Speed.speed
                + select(dto::speed) {
                    SpeedDto.all()
                        .map { it.id to it.description }
                        .sortedBy { it.second }
                }
            }
        }

    private fun description() =
        section(Strings.description, Strings.Ship.Description.explanation) {
            style { flexGrow = "1" }
            + textarea(dto::name) cssClass zkClasses.h100
        }

    private fun images() =
        section(Strings.images) {
            + Images(this@ShipForm, dto.id)
        }
}