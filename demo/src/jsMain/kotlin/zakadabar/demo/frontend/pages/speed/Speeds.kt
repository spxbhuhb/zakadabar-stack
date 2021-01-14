/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.speed

import zakadabar.demo.data.SpeedDto
import zakadabar.stack.frontend.elements.ZkCrud

object Speeds : ZkCrud<SpeedDto>() {
    init {
        companion = SpeedDto.Companion
        dtoClass = SpeedDto::class
        formClass = SpeedForm::class
        tableClass = SpeedTable::class
    }
}