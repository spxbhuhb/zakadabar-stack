/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.resources.css.ZkCssStyleRule
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssParameter
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkSideBarStyles by cssStyleSheet(ZkSideBarStyles())

open class ZkSideBarStyles : ZkCssStyleSheet() {

    open var backgroundColor by cssParameter { theme.backgroundColor }
    open var textColor by cssParameter { theme.textColor }
    open var fontSize by cssParameter { "80%" }
    open var hoverTextColor by cssParameter { theme.hoverTextColor }
    open var sectionBackgroundColor by cssParameter { theme.blockBackgroundColor }
    open var sectionTextColor by cssParameter { theme.textColor }
    open var sectionBorderColor by cssParameter { theme.borderColor }

    open val sidebar by cssClass {
        boxSizing = "border-box"
        minHeight = "100%"
        overflowY = "auto"
        padding = theme.spacingStep / 2

        fontSize = this@ZkSideBarStyles.fontSize
        backgroundColor = this@ZkSideBarStyles.backgroundColor
        color = this@ZkSideBarStyles.textColor

        small {
            padding = 0
        }

        medium {
            padding = 0
        }
    }

    open val item by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 28
        paddingRight = 8
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = this@ZkSideBarStyles.textColor

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor
        }

        on(" a") {
            paddingLeft = 20
            flexGrow = 1
            color = "inherit"
        }

        on(" > div") {
            paddingLeft = 20
            flexGrow = 1
        }
    }

    fun ZkCssStyleRule.title() {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 28
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-start"
        alignItems = "center"

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor
        }

        on(" a") {
            flexGrow = 1
            color = "inherit"
        }
    }

    open val groupTitle by cssClass {
        title()

        paddingRight = 8
        paddingLeft = 14

        fill = this@ZkSideBarStyles.textColor
        color = this@ZkSideBarStyles.textColor

    }

    open val groupContent by cssClass {
        paddingLeft = 20
    }

    open val sectionTitle by cssClass {
        title()

        marginTop = theme.spacingStep

        paddingRight = 8
        paddingLeft = 20

        backgroundColor = sectionBackgroundColor
        color = sectionTextColor

        //fill = this@ZkSideBarStyles.textColor.alpha(0.2)

        fill = sectionBorderColor
        borderBottom = "1px solid $sectionBorderColor"

        hover {

        }
        on(":first-child") {
            marginTop = 4
        }
    }

    open val sectionCloseIcon by cssClass {
        opacity = 0

        hover {
            opacity = 1
        }
    }

    open val sectionContent by cssClass {
        paddingLeft = 0
    }

    open val minimizedSectionContainer by cssClass {
        display = "flex"
        flexDirection = "row"
    }

    open val minimizedSection by cssClass {
        width = 28
        height = 28
        fontWeight = 500
        fontSize = "125%"
        display = "flex"
        justifyContent = "center"
        alignItems = "center"
        backgroundColor = theme.blockBackgroundColor
        borderBottom = theme.fixBorder
        cursor = "pointer"
        marginBottom = 6
    }
}