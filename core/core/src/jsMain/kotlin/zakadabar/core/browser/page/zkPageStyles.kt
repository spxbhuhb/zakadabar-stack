/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.page

import zakadabar.core.resource.css.*

var zkPageStyles by cssStyleSheet(ZkPageStyles())

open class ZkPageStyles : ZkCssStyleSheet() {

    /**
     * This style is meant for pages where the content of the page scrolls itself.
     * The overflow is `hidden`.
     */
    open val fixed by cssClass {
        + Position.relative
        + Overflow.hidden

        height = 100.percent
        maxHeight = 100.percent
    }

    /**
     * This style is meant for pages where the content of the page **does not** scroll itself.
     * The overflow is `auto`.
     */

    open val scrollable by cssClass {
        maxHeight = 100.percent
        + OverflowY.auto
    }

    /**
     * Adds the default margin.
     */
    open val content by cssClass {
        margin = theme.spacingStep.px
    }

}