/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkPageStyles by cssStyleSheet(ZkPageStyles())

class ZkPageStyles : ZkCssStyleSheet() {

    /**
     * This style is meant for pages where the content of the page scrolls itself.
     * The overflow is `hidden`.
     */
    val fixed by cssClass {
        height = "100%"
        maxHeight = "100%"
        overflow = "hidden"
    }

    /**
     * This style is meant for pages where the content of the page **does not** scroll itself.
     * The overflow is `auto`.
     */

    val scrollable by cssClass {
        maxHeight = "100%"
        overflowY = "auto"
    }

    /**
     * Adds the default margin.
     */
    val content by cssClass {
        margin = theme.spacingStep
    }

}