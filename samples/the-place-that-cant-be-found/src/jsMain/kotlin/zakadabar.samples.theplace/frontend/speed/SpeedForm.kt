/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend.speed

import zakadabar.samples.theplace.data.SpeedDto
import zakadabar.samples.theplace.frontend.form.ValidatedForm

class SpeedForm(dto: SpeedDto, mode: Mode) : ValidatedForm<SpeedDto>(dto, mode) {

    override fun init(): ValidatedForm<SpeedDto> {

        build {
            + dto::id
            + dto::description
            + dto::value
        }

        return this
    }

}