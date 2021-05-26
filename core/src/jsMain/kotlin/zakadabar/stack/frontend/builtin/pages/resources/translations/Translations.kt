/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.translations

import zakadabar.stack.data.builtin.resources.TranslationBo
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Translations : ZkCrudTarget<TranslationBo>() {

    init {
        companion = TranslationBo.Companion
        boClass = TranslationBo::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

