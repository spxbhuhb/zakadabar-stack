/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.crud.basic

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.crud.ZkInlineCrud

class BasicInlineCrud : ZkInlineCrud<ExampleBo>() {
    init {
        companion = ExampleBo.Companion
        boClass = ExampleBo::class
        editorClass = BasicInlineCrudForm::class
        tableClass = BasicInlineCrudTable::class
    }
}