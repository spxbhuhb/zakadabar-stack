/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.SimpleExampleDto
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget

object SimpleExampleCrud : ZkCrudTarget<SimpleExampleDto>() {
    init {
        companion = SimpleExampleDto.Companion
        dtoClass = SimpleExampleDto::class
        pageClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}