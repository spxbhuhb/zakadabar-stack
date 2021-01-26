/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.sea

import zakadabar.demo.data.SeaDto
import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SeaDto>() {

    init {
        title = Strings.seas
        onCreate = { Seas.openCreate() }

        + SeaDto::id
        + SeaDto::name
        + SeaDto::id.actions(Seas)
    }

}