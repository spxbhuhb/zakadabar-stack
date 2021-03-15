/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.speed.SpeedDto
import zakadabar.stack.frontend.builtin.ZkCrud

object Speeds : ZkCrud<SpeedDto>() {
    init {
        companion = SpeedDto.Companion
        dtoClass = SpeedDto::class
        formClass = Form::class
        tableClass = Table::class
    }
}