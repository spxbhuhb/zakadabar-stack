/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.page.ZkPageStyles
import zakadabar.core.resource.css.*

class SuiPageStyles : ZkPageStyles() {

    override val fixed by cssClass {
        + Position.relative
        + Overflow.hidden

        marginTop = (theme.spacingStep / 2).px

        height = 100.percent
        maxHeight = 100.percent
    }

    /**
     * This style is meant for pages where the content of the page **does not** scroll itself.
     * The overflow is `auto`.
     */

    override val scrollable by cssClass {
        maxHeight = 100.percent
        + OverflowY.auto
        + OverflowX.hidden
    }

}