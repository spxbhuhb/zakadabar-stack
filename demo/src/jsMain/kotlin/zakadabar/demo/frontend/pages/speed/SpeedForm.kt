/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.FormMode
import zakadabar.stack.frontend.builtin.form.ValidatedForm

class SpeedForm(dto: SpeedDto, mode: FormMode) : ValidatedForm<SpeedDto>(dto, mode, Speeds) {

    override fun init() = build {
        + header(Strings.Speed.speed)
        + section(Strings.basics, Strings.Speed.Basics.explanation) {
            + fieldGrid {
                + Strings.id
                + dto::id
                + Strings.description
                + dto::description
                + Strings.value
                + dto::value
            }
        }
        + buttons()
    }

}