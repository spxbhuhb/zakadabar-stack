/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.sea

import zakadabar.demo.marina.data.SeaDto
import zakadabar.demo.marina.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SeaDto>() {

    override fun onConfigure() {
        titleText = Strings.seas
        crud = Seas

        add = true
        search = true

        + SeaDto::id
        + SeaDto::name
        + actions()
    }

}