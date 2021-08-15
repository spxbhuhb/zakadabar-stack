/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.dock

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonDanger
import zakadabar.core.browser.button.buttonSecondary
import zakadabar.core.resource.ZkIcons

class DockRemoveExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        + buttonSecondary("Try It!") {
            zke {
                buttonDanger("Remove Me!") { application.dock -= this }
            }.dock(ZkIcons.menu, "hello")
        }
    }

}