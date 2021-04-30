/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.lib.frontend.crud

import zakadabar.demo.lib.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object BuiltinCrud : ZkCrudTarget<BuiltinDto>() {
    init {
        companion = BuiltinDto.Companion
        dtoClass = BuiltinDto::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}