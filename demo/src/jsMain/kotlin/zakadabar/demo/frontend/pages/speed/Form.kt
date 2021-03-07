/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.demo.frontend.resources.DemoStrings.Companion.demo
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<SpeedDto>() {

    override fun onCreate() {

        + titleBar(dto.description, demo.speed)

        + column(ZkFormStyles.contentContainer) {
            + section(demo.basics, demo.speedBasicsExplanation) {

                ifNotCreate {
                    + dto::id
                }

                + dto::description

                + dto::value
            }

            + buttons()
        }
    }

}