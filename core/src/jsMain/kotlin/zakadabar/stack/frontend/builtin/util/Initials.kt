/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

class Initials(private val displayName: String) : ZkElement() {

    override fun init(): Initials {

        innerText = if (displayName.length < 2) {
            displayName.toUpperCase()
        } else {
            val e = displayName.split(" ")
            if (e.size < 2) {
                displayName.substring(0, 2).toUpperCase()
            } else {
                "${e[0][0].toUpperCase()}${e[1][0].toUpperCase()}"
            }
        }

        className = coreClasses.avatar

        return this
    }

}
