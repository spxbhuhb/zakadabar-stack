/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.marina.frontend.pages.speed

import zakadabar.demo.marina.data.speed.SpeedDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Speeds : ZkCrudTarget<SpeedDto>() {
    init {
        companion = SpeedDto.Companion
        dtoClass = SpeedDto::class
        pageClass = Form::class
        tableClass = Table::class
    }
}