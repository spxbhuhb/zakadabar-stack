/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.resources.locales

import zakadabar.stack.data.builtin.resources.LocaleBo
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget

object Locales : ZkCrudTarget<LocaleBo>() {

    init {
        companion = LocaleBo.Companion
        boClass = LocaleBo::class
        tableClass = Table::class
        pageClass = Form::class
    }

}

