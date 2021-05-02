/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.ZkElement

/**
 * Element to shown in the application title bar as the title of the current page.
 */
open class ZkAppTitle(
    val text: String,
    val contextElements: List<ZkElement> = emptyList()
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        + text
    }

}