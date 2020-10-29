/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import zakadabar.stack.frontend.elements.ZkElement

open class SimpleIcon(val icon: String) : ZkElement() {

    override fun init(): SimpleIcon {
        element.innerHTML = icon
        return this
    }

}