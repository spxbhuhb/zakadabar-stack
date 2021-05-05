/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object BuiltinCrud : ZkCrudTarget<zakadabar.lib.examples.data.builtin.BuiltinDto>() {
    init {
        companion = zakadabar.lib.examples.data.builtin.BuiltinDto.Companion
        dtoClass = zakadabar.lib.examples.data.builtin.BuiltinDto::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}