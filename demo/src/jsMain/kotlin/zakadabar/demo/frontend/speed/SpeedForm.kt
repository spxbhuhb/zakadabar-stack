/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.form.ValidatedForm

class SpeedForm(dto: SpeedDto, mode: Mode) : ValidatedForm<SpeedDto>(dto, Speeds, mode) {

    override fun init() = build {
        + header("Speed")
        + section("Basics", "Data that all speeds have.") {
            + dto::id
            + dto::description
            + dto::value
        }
        + buttons()
    }

}