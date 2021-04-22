/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.settings

import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Settings : ZkCrudTarget<SettingDto>() {

    init {
        companion = SettingDto.Companion
        dtoClass = SettingDto::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

