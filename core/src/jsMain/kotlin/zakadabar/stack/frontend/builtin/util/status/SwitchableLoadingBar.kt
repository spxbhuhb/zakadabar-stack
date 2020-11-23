/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.status

import zakadabar.stack.frontend.builtin.util.SwitchableElement
import zakadabar.stack.frontend.elements.ZkElement

class SwitchableLoadingBar : SwitchableElement() {

    override fun init(): ZkElement {
        innerHTML = "...loading..." // FIXME implement loading bar
        return this
    }

}