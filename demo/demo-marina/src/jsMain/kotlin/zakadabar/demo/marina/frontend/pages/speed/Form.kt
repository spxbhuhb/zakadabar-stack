/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.speed

import zakadabar.demo.marina.data.speed.SpeedDto
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<SpeedDto>() {

    override fun onCreate() {

        build(dto.description, Strings.speed) {

            + section(Strings.basics, Strings.speedBasicsExplanation) {
                + dto::id
                + dto::description
                + dto::value
            }

        }
    }

}