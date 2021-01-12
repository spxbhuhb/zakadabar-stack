/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.builtin.table.ZkTable

class SpeedTable : ZkTable<SpeedDto>() {

    init {
        + SpeedDto::id
        + SpeedDto::description
        + SpeedDto::value
        + SpeedDto::id.actions(Speeds)
    }

}