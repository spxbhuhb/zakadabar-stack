/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkSideBarStyles by cssStyleSheet(ZkSideBarStyles())

open class ZkSideBarStyles : ZkCssStyleSheet() {

    open var backgroundColor: String? = null
    open var textColor: String? = null
    open var fontSize: String = "80%"
    open var hoverTextColor: String? = null

    open val sidebar by cssClass {
        boxSizing = "border-box"
        minHeight = "100%"
        overflowY = "auto"
        padding = theme.spacingStep / 2

        fontSize = this@ZkSideBarStyles.fontSize

        this@ZkSideBarStyles.backgroundColor?.let { backgroundColor = this@ZkSideBarStyles.backgroundColor }
        this@ZkSideBarStyles.textColor?.let { color = this@ZkSideBarStyles.textColor }
        this@ZkSideBarStyles.backgroundColor?.let { backgroundColor = this@ZkSideBarStyles.backgroundColor }

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

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor ?: theme.hoverTextColor
        }

        on(" a") {
            paddingLeft = 20
            flexGrow = 1
        }

        on(" > div") {
            paddingLeft = 20
            flexGrow = 1
        }
    }

    open val groupTitle by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 28
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-start"
        alignItems = "center"
        paddingRight = 8
        paddingLeft = 14
        fill = this@ZkSideBarStyles.textColor ?: theme.textColor

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = theme.hoverTextColor
        }

        on(" a") {
            flexGrow = 1
        }
    }

    open val groupContent by cssClass {
        paddingLeft = 20
    }

    open val sectionTitle by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 28
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-start"
        alignItems = "center"
        paddingRight = 8
        paddingLeft = 20
        marginTop = theme.spacingStep
        backgroundColor = theme.blockBackgroundColor
        fill = this@ZkSideBarStyles.textColor ?: theme.textColor
        borderBottom = theme.fixBorder

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = theme.hoverTextColor
        }

        on(" a") {
            flexGrow = 1
        }

        on(":first-child") {
            marginTop = 4
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
    }
}