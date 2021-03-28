/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.sea

import zakadabar.demo.data.SeaDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<SeaDto>() {

    override fun onCreate() {

        + titleBar(dto.name, Strings.sea)

        + section(Strings.basics) {

            ifNotCreate {
                + dto::id
            }

            + dto::name

        }

        + buttons()
    }

}