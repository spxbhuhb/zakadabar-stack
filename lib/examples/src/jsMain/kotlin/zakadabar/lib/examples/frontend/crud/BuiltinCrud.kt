/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.crud.ZkInlineCrud

class BuiltinCrud : ZkInlineCrud<BuiltinDto>() {
    init {
        companion = BuiltinDto.Companion
        dtoClass = BuiltinDto::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}