/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.speed.SpeedDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles

class Form : ZkForm<SpeedDto>() {

    override fun onCreate() {

        + build(dto.description, Strings.speed) {

            + section(Strings.basics, Strings.speedBasicsExplanation) {
                + dto::id
                + dto::description
                + dto::value
            }

        }
    }

}