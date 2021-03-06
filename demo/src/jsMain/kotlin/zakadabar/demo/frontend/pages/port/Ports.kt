/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.port

import zakadabar.demo.data.PortDto
import zakadabar.stack.frontend.builtin.ZkCrud

object Ports : ZkCrud<PortDto>() {
    init {
        companion = PortDto.Companion
        dtoClass = PortDto::class
        formClass = Form::class
        tableClass = Table::class
    }
}