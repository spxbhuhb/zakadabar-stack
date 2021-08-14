/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.misc.account

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.util.PublicApi

@PublicApi
class ZkInitials(val name: String) : ZkElement() {

    // TODO this class should be more sophisticated

    override fun onCreate() {

        + zkAccountStyles.avatar

        innerText = if (name.length < 2) {
            name.uppercase()
        } else {
            val e = name.split(" ")
            if (e.size < 2) {
                name.substring(0, 2).uppercase()
            } else {
                "${e[0][0].uppercaseChar()}${e[1][0].uppercaseChar()}"
            }
        }

    }

}
