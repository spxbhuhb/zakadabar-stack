/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.port

import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.form.ZkForm

class Form : ZkForm<PortDto>() {

    override fun init() = build {
        + header(Strings.port)

        + section(Strings.basics) {

            ifNotCreate {
                + dto::id
            }

            + dto::name

            + select(dto::sea) {
                SeaDto.all()
                    .map { it.id to it.name }
                    .sortedBy { it.second }
            }
        }

        + buttons()
    }

}