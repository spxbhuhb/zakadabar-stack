/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.port

import zakadabar.demo.data.PortDto
import zakadabar.demo.data.SeaDto
import zakadabar.demo.frontend.resources.DemoStrings.Companion.demo
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<PortDto>() {

    private val seas by preload { SeaDto.allAsMap() }

    init {
        title = demo.ports
        crud = Ports

        + PortDto::id

        + custom {
            label = demo.sea
            render = { + seas[it.sea]?.name }
        }

        + PortDto::name
        + actions()
    }

}