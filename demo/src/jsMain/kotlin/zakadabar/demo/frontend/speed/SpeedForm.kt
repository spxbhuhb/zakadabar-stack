/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.R
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ValidatedForm

class SpeedForm(dto: SpeedDto, mode: FormMode) : ValidatedForm<SpeedDto>(dto, mode, Speeds) {

    override fun init() = build {
        + header(R.Speed.speed)
        + section(R.basics, R.Speed.Basics.explanation) {
            + fieldGrid {
                + R.id
                + dto::id
                + R.description
                + dto::description
                + R.value
                + dto::value
            }
        }
        + buttons()
    }

}