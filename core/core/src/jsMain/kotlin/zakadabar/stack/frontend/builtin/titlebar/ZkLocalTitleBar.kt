/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
open class ZkLocalTitleBar(
    open val text: String,
    open val contextElements: List<ZkElement> = emptyList()
) : ZkElement() {

    override fun onCreate() {
        + zkTitleBarStyles.localTitleBar

        + div { + text }
        + row {
            contextElements.forEach {
                + it marginRight 10
            }
        }
    }

}
