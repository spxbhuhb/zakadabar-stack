/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.sea

import zakadabar.demo.marina.data.SeaDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Seas : ZkCrudTarget<SeaDto>() {
    init {
        companion = SeaDto.Companion
        dtoClass = SeaDto::class
        pageClass = Form::class
        tableClass = Table::class
    }
}