/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.core.resource.css.*

val siteStyles by cssStyleSheet(SiteStyles())

class SiteStyles : ZkCssStyleSheet() {

    val developerLogoUrl: String = "/simplexion_logo.svg"

    val headerContent by cssClass {
        + Display.grid
        width = 100.percent
        gridTemplateColumns = "max-content max-content min-content 1fr max-content"
        color = theme.infoPair
    }

    val siteName by cssClass {
        + AlignSelf.center
        + Cursor.pointer
        fontSize = 14.px
        marginLeft = 40.px
        marginRight = 40.px
    }

    val version by cssClass {
        + AlignSelf.center
        fontSize = 14.px
    }

    val headerLinks by cssClass {
        + Display.flex
        + JustifyContent.spaceEvenly
        + AlignItems.center
    }

    val headerLink by cssClass {
        fontSize = 14.px
    }

    val sideBarToggle by cssClass {
        + AlignSelf.center
        paddingLeft = 20.px
        fill = theme.infoPair
        + Cursor.pointer
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