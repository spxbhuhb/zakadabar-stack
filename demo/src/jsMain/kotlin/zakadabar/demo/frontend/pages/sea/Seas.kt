/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.sea

import zakadabar.demo.data.SeaDto
import zakadabar.stack.frontend.elements.ZkCrud

object Seas : ZkCrud<SeaDto>() {
    init {
        companion = SeaDto.Companion
        dtoClass = SeaDto::class
        formClass = Form::class
        tableClass = Table::class
    }
}