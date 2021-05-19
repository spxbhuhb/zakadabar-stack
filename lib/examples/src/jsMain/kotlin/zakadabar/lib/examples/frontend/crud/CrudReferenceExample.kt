/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles

class CrudReferenceExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        height = 400
        + zkLayoutStyles.fixBorder
        
        + ExampleReferenceCrud().apply { openAll() }
    }
}