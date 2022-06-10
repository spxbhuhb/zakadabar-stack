/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.*

val landingStyles by cssStyleSheet(LandingStyles())

class LandingStyles : ZkCssStyleSheet() {

    val landing by cssClass {
        + Display.grid
        gridTemplateRows = "max-content 1fr max-content"
        width = 100.percent
        height = 100.percent
        backgroundColor = theme.backgroundColor
        padding = 0.px + "!important"
    }

    val header by cssClass {
        + Display.flex
        + AlignItems.center
        + JustifyContent.spaceBetween

        height = 60.px

        paddingLeft = 50.px

        backgroundColor = if (theme.name == SiteGreenBlueTheme.NAME) theme.blockBackgroundColor else theme.backgroundColor
        borderBottom = if (theme.name == SiteGreenBlueTheme.NAME) theme.fixBorder else theme.border
    }

    val content by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.column
        + AlignItems.center
        + JustifySelf.center
        + Overflow.auto

        paddingTop = 50.px
        maxWidth = 1168.px

        small {
            paddingLeft = 10.px
            paddingRight = 10.px
        }

        medium {
            marginLeft = 20.px
            marginLeft = 20.px
        }

        large {
            marginLeft = 30.px
            marginLeft = 30.px
        }
    }

    val title by cssClass {
        fontSize = 48.px
    }

    val buttons by cssClass {
        + Display.flex
        + FlexDirection.row
        flexWrap = "wrap"
    }

    private fun ZkCssStyleRule.button(color: String) {
        border = "1px solid $color"
        marginRight = 20.px
        on(":last-child") {
            marginRight = 0.px
        }
    }

    val buttonCyan by cssClass {
        button(ZkColors.Zakadabar.navCyan)
    }

    val buttonBlue by cssClass {
        button(ZkColors.Zakadabar.navBlue)
    }

    val buttonGreen by cssClass {
        button(ZkColors.Zakadabar.navGreen)
    }

    val buttonOrange by cssClass {
        button(ZkColors.Zakadabar.navOrange)
    }

    val buttonRed by cssClass {
        button(ZkColors.Zakadabar.navRed)
    }

    val cards by cssClass {
        gridTemplateColumns = "repeat( auto-fit, minmax( 250px, 1fr ) )"
        gap = 20.px
        + AlignSelf.stretch
    }

    val card by cssClass {
        + Display.flex
        + FlexDirection.row
        + JustifyContent.center

        + AlignSelf.stretch
    }

    val cardInner by cssClass {
        width = 250.px
        border = theme.border
        borderRadius = 2.px
    }

    val cardTitle by cssClass {
        fontSize = 18.px
        padding = 20.px
        fontWeight = 500.weight
        borderBottom = theme.border
    }

    val cardText by cssClass {
        padding = 20.px
    }

    val footer by cssClass {
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        paddingLeft = 50.px
        paddingBottom = 8.px
        paddingTop = 8.px

        borderTop = theme.border
    }

}