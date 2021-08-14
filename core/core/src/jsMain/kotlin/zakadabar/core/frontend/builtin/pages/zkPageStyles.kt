/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.pages

import zakadabar.core.frontend.resources.css.*

val zkPageStyles by cssStyleSheet(ZkPageStyles())

class ZkPageStyles : ZkCssStyleSheet() {

    /**
     * This style is meant for pages where the content of the page scrolls itself.
     * The overflow is `hidden`.
     */
    val fixed by cssClass {
        + Position.relative
        + Overflow.hidden

        height = 100.percent
        maxHeight = 100.percent
    }

    /**
     * This style is meant for pages where the content of the page **does not** scroll itself.
     * The overflow is `auto`.
     */

    val scrollable by cssClass {
        maxHeight = 100.percent
        + OverflowY.auto
    }

    /**
     * Adds the default margin.
     */
    val content by cssClass {
        margin = theme.spacingStep.px
    }

}