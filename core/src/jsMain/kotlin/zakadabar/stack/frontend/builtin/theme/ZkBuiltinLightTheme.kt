/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
import zakadabar.stack.frontend.builtin.form.ZkFormTheme
import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.layout.ZkScrollBarTheme
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainerTheme
import zakadabar.stack.frontend.builtin.note.ZkNoteTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarTheme
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.after
import zakadabar.stack.util.alpha

open class ZkBuiltinLightTheme : ZkTheme {

    companion object {
        const val NAME = "zakadabar.stack.theme.light"
    }

    override val name = NAME

    override var fontFamily = "'Lato', sans-serif" //     "'IBM Plex Sans', sans-serif"
    override var fontSize: String = "16px"
    override var fontWeight: String = "300"

    override var backgroundColor = ZkColors.white
    override var textColor = ZkColors.Design.gray8

    override var hoverBackgroundColor = "rgba(0,0,0,0.1)"
    override var hoverTextColor = ZkColors.Design.gray8

    override var primaryColor = ZkColors.Design.navPurple
    override var primaryPair = ZkColors.white
    override var secondaryColor = ZkColors.Design.gray5
    override var secondaryPair = ZkColors.white
    override var successColor = ZkColors.Design.navGreen
    override var successPair = ZkColors.white
    override var warningColor = ZkColors.Design.navOrange
    override var warningPair = ZkColors.white
    override var dangerColor = ZkColors.Design.navRed
    override var dangerPair = ZkColors.white
    override var infoColor = ZkColors.Design.navBlue
    override var infoPair = ZkColors.white
    override var disabledColor = ZkColors.Design.gray2
    override var disabledPair = ZkColors.Design.gray6

    override var inputTextColor by after { textColor }
    override var inputBackgroundColor by after { backgroundColor }
    override var disabledInputColor by after { disabledColor }
    override var disabledInputPair by after { disabledPair }

    override var borderColor = "transparent" // ZkColors.Gray.c600.alpha(0.5)

    override var border by after { "1px solid $borderColor" }

    override var cornerRadius = 2
    override var spacingStep = 20

    val background = ZkColors.white
    val foreground = ZkColors.Design.gray8

    val success = ZkColors.Design.navGreen
    val info = ZkColors.Design.navBlue
    val warning = ZkColors.Design.navOrange
    val error = ZkColors.Design.navRed

    override var dock = ZkDockTheme()

    override var form = ZkFormTheme()

    override var layout = ZkLayoutTheme(
        defaultForeground = foreground,
        defaultBackground = background
    )

    override var note = ZkNoteTheme(
        background = ZkColors.Gray.c50
    )

    override var scrollBar by after {
        ZkScrollBarTheme(
            width = 12,
            height = 12,
            background = backgroundColor,
            foreground = backgroundColor.alpha(0.5)
        )
    }

    override var tabContainer = ZkTabContainerTheme()
    override var table = ZkTableTheme()

    override var titleBar by after {
        ZkTitleBarTheme(
            appTitleBarHeight = "44px",
            localTitleBarHeight = "44px",
            appHandleBackground = ZkColors.Design.gray1,
            appHandleText = textColor,
            appHandleBorder = border,
            titleBarBackground = ZkColors.Design.gray1,
            titleBarText = textColor,
            titleBarBorder = border
        )
    }

}