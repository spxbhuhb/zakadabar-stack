/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.resources

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object LandingStyles : ZkCssStyleSheet() {

    val landing by cssClass {
        display = "grid"
        gridTemplateRows = "max-content 1fr max-content"
        width = "100vw"
        height = "100vh"
        backgroundColor = theme.layout.defaultBackground
        color = theme.layout.defaultForeground
    }

    val header by cssClass {
        paddingLeft = 50
        display = "flex"
        alignItems = "center"
        justifyContent = "space-between"
        height = 60
        backgroundColor = theme.layout.defaultBackground
        borderBottom = "1px solid ${ZkColors.Design.navPurple}"
    }

    val content by cssClass {
        padding = 50
        overflow = "auto"
    }

    val title by cssClass {
        fontSize = 48
    }

    val buttons by cssClass {
        display = "flex"
        flexDirection = "row"
        flexWrap = "wrap"
    }

    val button by cssClass {
        fontSize = "16px !important"
        fontWeight = 400
        whiteSpace = "nowrap"
        marginTop = 20
        color = "${theme.layout.defaultForeground} !important"
        backgroundColor = "transparent !important"
    }

    val buttonCyan by cssClass {
        border = "1px solid ${ZkColors.Design.navCyan}"
    }

    val buttonBlue by cssClass {
        border = "1px solid ${ZkColors.Design.navBlue}"
    }

    val buttonGreen by cssClass {
        border = "1px solid ${ZkColors.Design.navGreen}"
    }

    val buttonOrange by cssClass {
        border = "1px solid ${ZkColors.Design.navOrange}"
    }

    val buttonRed by cssClass {
        border = "1px solid ${ZkColors.Design.navRed}"
    }

    val cards by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-around"
        flexWrap = "wrap"
    }

    val card by cssClass {
        marginTop = 50
        marginRight = 20
        border = "1px solid ${ZkColors.Gray.c600}"
        width = 250
        borderRadius = 2
    }

    val cardTitle by cssClass {
        fontSize = 18
        padding = 20
        fontWeight = 500
        borderBottom = "1px solid ${ZkColors.Gray.c600}"
    }

    val cardText by cssClass {
        padding = 20
    }

    val footer by cssClass {
        paddingTop = 20
        paddingBottom = 20
        paddingLeft = 50
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    init {
        attach()
    }
}