/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonSecondary
import zakadabar.core.util.PublicApi

@PublicApi
fun ZkElement.withHelp(func: () -> ZkElement?): HTMLElement? {

    return func()?.let {
        + row {
            + it
            + buttonSecondary("?") { }
        }
    }

}