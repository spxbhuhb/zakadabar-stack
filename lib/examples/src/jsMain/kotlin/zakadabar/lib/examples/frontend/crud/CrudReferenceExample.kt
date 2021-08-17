/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.resource.css.px

class CrudReferenceExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        height = 400.px
        + zkLayoutStyles.fixBorder
        
        + ExampleReferenceInlineCrud().apply { openAll() }
    }
}