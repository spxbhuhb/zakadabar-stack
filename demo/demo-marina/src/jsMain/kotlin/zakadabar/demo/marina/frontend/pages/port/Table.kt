/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.port

import zakadabar.demo.marina.data.PortDto
import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<PortDto>() {

    private val seas by preload { SeaDto.allAsMap() }

    override fun onConfigure() {
        titleText = Strings.ports
        crud = Ports

        add = true
        search = true

        + PortDto::id

        + custom {
            label = Strings.sea
            render = { + seas[it.sea]?.name }
        }

        + PortDto::name
        + actions()
    }

}