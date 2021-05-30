/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val landingStyles by cssStyleSheet(LandingStyles())

class LandingStyles : ZkCssStyleSheet() {

    val landing by cssClass {
        display = "grid"
        gridTemplateRows = "max-content 1fr max-content"
        width = "100%"
        height = "100%"
        backgroundColor = theme.backgroundColor
    }

    val header by cssClass {
        paddingLeft = 50
        display = "flex"
        alignItems = "center"
        justifyContent = "space-between"
        height = 60
        backgroundColor = if (theme.name == SiteGreenBlueTheme.NAME) theme.blockBackgroundColor else theme.backgroundColor
        borderBottom = if (theme.name == SiteGreenBlueTheme.NAME) theme.fixBorder else theme.border
    }

    val content by cssClass {
        boxSizing = "border-box"
        paddingTop = 50
        maxWidth = 1168
        justifySelf = "center"
        overflow = "auto"
        alignItems = "center"

        small {
            paddingLeft = 10
            paddingRight = 10
        }

        medium {
            marginLeft = 20
            marginLeft = 20
        }

        large {
            marginLeft = 30
            marginLeft = 30
        }
    }

    val title by cssClass {
        fontSize = 48
    }

    val buttons by cssClass {
        display = "flex"
        flexDirection = "row"
        flexWrap = "wrap"
    }

    private fun ZkCssStyleRule.button(color : String) {
        border = "1px solid $color"
        marginRight = 20
        on(":last-child") {
            marginRight = 0
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
        gap = 20
        alignSelf = "stretch"
    }

    val card by cssClass {
        alignSelf = "stretch"
        display = "flex"
        flexDirection = "row"
        justifyContent = "center"
    }

    val cardInner by cssClass {
        width = 250
        border = theme.border
        borderRadius = 2
    }

    val cardTitle by cssClass {
        fontSize = 18
        padding = 20
        fontWeight = 500
        borderBottom = theme.border
    }

    val cardText by cssClass {
        padding = 20
    }

    val footer by cssClass {
        paddingLeft = 50
        paddingBottom = 8
        paddingTop = 8
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        borderTop = theme.border
    }

}