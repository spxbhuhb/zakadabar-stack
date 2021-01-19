/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<SpeedDto>() {

    override fun init() = build {
        + header(Strings.speed)

        + section(Strings.basics, Strings.speedBasicsExplanation) {
            + fieldGrid {

                ifNotCreate {
                    + Strings.id
                    + dto::id
                }

                + Strings.description
                + dto::description

                + Strings.value
                + dto::value
            }
        }

        + buttons()
    }

}