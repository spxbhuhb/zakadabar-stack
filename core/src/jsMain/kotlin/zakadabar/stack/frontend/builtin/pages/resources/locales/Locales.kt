/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Locales : ZkCrudTarget<LocaleDto>() {

    init {
        companion = LocaleDto.Companion
        dtoClass = LocaleDto::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

