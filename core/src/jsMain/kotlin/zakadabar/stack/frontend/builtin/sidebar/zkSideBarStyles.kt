/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkSideBarStyles by cssStyleSheet(ZkSideBarStyles())

open class ZkSideBarStyles : ZkCssStyleSheet<ZkTheme>() {

    open var backgroundColor: String? = null
    open var textColor: String? = null
    open var fontSize: String = "80%"
    open var hoverTextColor: String? = null

    open val sidebar by cssClass {
        boxSizing = "border-box"
        minHeight = "100%"
        overflowY = "auto"
        minWidth = 220
        paddingTop = 10
        fontSize = this@ZkSideBarStyles.fontSize

        this@ZkSideBarStyles.backgroundColor?.let { backgroundColor = this@ZkSideBarStyles.backgroundColor }
        this@ZkSideBarStyles.textColor?.let { color = this@ZkSideBarStyles.textColor }
        this@ZkSideBarStyles.backgroundColor?.let { backgroundColor = this@ZkSideBarStyles.backgroundColor }

    }

    open val item by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 28
        paddingLeft = 20
        paddingRight = 8
        display = "flex"
        flexDirection = "row"
        alignItems = "center"

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@ZkSideBarStyles.hoverTextColor ?: theme.hoverTextColor
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
    }

    open val groupContent by cssClass {
        paddingLeft = 20
    }

}