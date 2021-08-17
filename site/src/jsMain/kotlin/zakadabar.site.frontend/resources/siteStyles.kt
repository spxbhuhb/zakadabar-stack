/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.core.resource.css.*

val siteStyles by cssStyleSheet(SiteStyles())

class SiteStyles : ZkCssStyleSheet() {

    val developerLogoUrl: String = "/simplexion_logo.svg"

    val logo by cssClass {
        + Cursor.pointer
        height = 22.px
        fill = theme.textColor
    }

    val headerActions by cssClass {
        + AlignItems.center
    }

    val developerLogo by cssClass {
        width = 48.px
        height = 48.px
        fill = theme.textColor
    }

    val title by cssClass {
        + JustifyContent.spaceAround
        + AlignItems.center
    }

    val alphaStyle by cssClass {
        backgroundColor = theme.warningColor
        color = theme.warningPair
        fontSize = 75.percent
        fontFamily = "'JetBrains Mono', fixed"
        marginLeft = 10.px
        padding = "2px 6px 2px 6px"
        borderRadius = theme.cornerRadius.px
        + Cursor.pointer
    }

    val cookbook by cssClass {
        + Display.flex
        + FlexDirection.column
        + AlignItems.center
        paddingTop = theme.spacingStep.px
        paddingBottom = theme.spacingStep.px
    }

    val cookbookCard by cssClass {
        width = 800.px
        padding = theme.spacingStep.px
        margin = theme.spacingStep.px
        border = "1px solid ${theme.borderColor}"
        borderRadius = theme.cornerRadius.px
        boxShadow = theme.boxShadow
    }

    val cookbookCardTitle by cssClass {
        fontWeight = 500.weight
    }

}