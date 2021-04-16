/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.ZkElement

open class ZkPageTitle(
    val text: String,
    val contextElements: List<ZkElement> = emptyList()
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        + text
    }

}