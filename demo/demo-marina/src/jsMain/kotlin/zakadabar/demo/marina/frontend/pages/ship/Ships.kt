/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.ship

import zakadabar.demo.marina.data.ship.ShipDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Ships : ZkCrudTarget<ShipDto>() {

    init {
        companion = ShipDto.Companion
        dtoClass = ShipDto::class
        pageClass = Form::class
        tableClass = Table::class
    }

}

