/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.util.PublicApi

@PublicApi
open class ZkLocalTitleBar(
    open val text: String,
    open val contextElements: List<ZkElement> = emptyList(),
    open val icon: ZkIconSource? = null
) : ZkElement() {

    override fun onCreate() {
        + zkTitleBarStyles.localTitleBar

        + div {
            icon?.let {
                + grid {
                    + zkTitleBarStyles.localTitleAndIcon
                    + grid { + ZkIcon(it) }
                    + grid { + text }
                }
            } ?: run {
                + text
            }
        }
        + row {
            contextElements.forEach {
                + it marginRight 10
            }
        }
    }

}
