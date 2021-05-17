/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.dock

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.buttonSecondary
import zakadabar.stack.frontend.resources.ZkIcons

class DockBasicExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + buttonSecondary("Try It!") {
            zke { + "Hello World!" }.dock(ZkIcons.account_box, "hello")
        }
    }

}