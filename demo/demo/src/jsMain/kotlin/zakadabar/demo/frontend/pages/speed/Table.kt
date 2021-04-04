/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.speed.SpeedDto
import zakadabar.demo.resources.Strings
import zakadabar.stack.frontend.builtin.table.ZkTable

class Table : ZkTable<SpeedDto>() {

    override fun onConfigure() {
        title = Strings.speeds
        crud = Speeds

        add = true
        search = true
        export = true

        + SpeedDto::id
        + SpeedDto::description
        + SpeedDto::value

        + actions()
    }

}