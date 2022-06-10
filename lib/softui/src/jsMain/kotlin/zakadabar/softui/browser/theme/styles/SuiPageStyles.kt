/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.page.ZkPageStyles
import zakadabar.core.resource.css.*

open class SuiPageStyles : ZkPageStyles() {

    fun ZkCssStyleRule.padding(large : String, small : String) {
        paddingLeft = large
        paddingRight = large
        paddingBottom = large

        medium {
            paddingLeft = small
            paddingRight = small
            paddingBottom = small
        }
    }

    override val fixed by cssClass {
        + BoxSizing.borderBox
        + Position.relative
        + Overflow.hidden
        height = 100.percent
        maxHeight = 100.percent

        padding(20.px, 10.px)
    }

    /**
     * This style is meant for pages where the content of the page **does not** scroll itself.
     * The overflow is `auto`.
     */

    override val scrollable by cssClass {
        + BoxSizing.borderBox
        maxHeight = 100.percent
        + OverflowY.auto
        + OverflowX.hidden

        padding(20.px, 10.px)
    }

    override val content by cssClass {

    }

}