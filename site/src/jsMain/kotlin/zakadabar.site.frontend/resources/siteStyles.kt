/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val siteStyles by cssStyleSheet(SiteStyles())

class SiteStyles : ZkCssStyleSheet() {

    val developerLogoUrl: String = "/simplexion_logo.svg"

    val logo by cssClass {
        cursor = "pointer"
        height = 22
        fill = theme.textColor
    }

    val headerActions by cssClass {
        alignItems = "center"
    }

    val developerLogo by cssClass {
        width = 48
        height = 48
        fill = theme.textColor
    }

    val title by cssClass {
        justifyContent = "space-around"
        alignItems = "center"
    }

    val alphaStyle by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fontSize = "75%"
        fontFamily = "'JetBrains Mono', fixed"
        marginLeft = 10
        padding = "2px 6px 2px 6px"
        borderRadius = theme.cornerRadius
        cursor = "pointer"
    }

}