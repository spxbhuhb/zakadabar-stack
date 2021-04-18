/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object SiteStyles : ZkCssStyleSheet() {

    val developerLogo by cssClass {
        backgroundImage = (theme as SiteTheme).developerLogo
        width = 24
        height = 24
        marginRight = 16
    }

    val header by cssClass {
        paddingLeft = theme.layout.paddingStep * 2
        borderBottom = "1px solid ${ZkColors.Design.navPurple}"
    }

    val logo by cssClass {
        height = 22
        fill = theme.layout.defaultForeground
    }

    val headerActions by cssClass {
        alignItems = "center"
    }

    val headerButton by cssClass {
        color = "${theme.layout.defaultForeground} !important"
        backgroundColor = "transparent !important"
    }

    init {
        attach()
    }
}