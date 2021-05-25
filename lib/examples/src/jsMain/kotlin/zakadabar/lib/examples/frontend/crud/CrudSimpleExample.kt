/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.crud.ZkInlineCrud
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles

class SimpleExampleInlineCrud : ZkInlineCrud<SimpleExampleBo>() {
    init {
        companion = SimpleExampleBo.Companion
        boClass = SimpleExampleBo::class
        editorClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}

class CrudSimpleExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        height = 400
        + zkLayoutStyles.fixBorder
        
        + SimpleExampleInlineCrud().apply { openAll() }
    }
}