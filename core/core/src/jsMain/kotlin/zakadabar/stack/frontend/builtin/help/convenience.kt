/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.help

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.buttonSecondary
import zakadabar.stack.util.PublicApi

@PublicApi
fun ZkElement.withHelp(func: () -> ZkElement?): HTMLElement? {

    return func()?.let {
        + div {
            + it
            + buttonSecondary("?") { }
        }
    }

}