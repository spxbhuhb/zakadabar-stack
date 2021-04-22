/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.translations

import zakadabar.stack.data.builtin.resources.TranslationDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object Translations : ZkCrudTarget<TranslationDto>() {

    init {
        companion = TranslationDto.Companion
        dtoClass = TranslationDto::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

