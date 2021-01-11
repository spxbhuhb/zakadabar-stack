/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.ship

import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ValidatedForm
import zakadabar.stack.frontend.builtin.form.fields.Images
import zakadabar.stack.frontend.elements.ZkClasses.Companion.zkClasses

class ShipForm(dto: ShipDto, mode: FormMode) : ValidatedForm<ShipDto>(dto, mode, Ships) {

    override fun init() = build {
        + header(Strings.Ship.ships)
        + column {
            + row {
                + section(Strings.basics, Strings.Ship.Basics.explanation) {
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
                } marginRight 8
                + section(Strings.description, Strings.Ship.Description.explanation) {
                    style { flexGrow = "1" }
                    + textarea(dto::name) cssClass zkClasses.h100
                }
            }
            + section(Strings.images) {
                + Images(this@ShipForm, dto.id)
            }
            + buttons()
        }
    }

}