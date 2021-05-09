/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
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
    override var textColor = ZkColors.Zakadabar.gray8

    override var hoverBackgroundColor = "rgba(0,0,0,0.1)"
    override var hoverTextColor = ZkColors.Zakadabar.gray8

    override var primaryColor = ZkColors.Zakadabar.navPurple
    override var primaryPair = ZkColors.white
    override var secondaryColor = ZkColors.Zakadabar.gray5
    override var secondaryPair = ZkColors.white
    override var successColor = ZkColors.Zakadabar.navGreen
    override var successPair = ZkColors.white
    override var warningColor = ZkColors.Zakadabar.navOrange
    override var warningPair = ZkColors.white
    override var dangerColor = ZkColors.Zakadabar.navRed
    override var dangerPair = ZkColors.white
    override var infoColor = ZkColors.Zakadabar.navBlue
    override var infoPair = ZkColors.white
    override var disabledColor = ZkColors.Zakadabar.gray2
    override var disabledPair = ZkColors.Zakadabar.gray6

    override var inputTextColor by after { textColor }
    override var inputBackgroundColor = ZkColors.white

    override var disabledInputTextColor = ZkColors.white
    override var disabledInputBackgroundColor = ZkColors.Zakadabar.gray4

    override var borderColor = ZkColors.Gray.c600.alpha(0.5)

    override var border = "transparent"

    override var cornerRadius = 2
    override var spacingStep = 20

    override var boxShadow = "none" // "0 2px 2px 0 rgba(0, 0, 0, 0.14), 0 3px 1px -2px rgba(0, 0, 0, 0.2), 0 1px 5px 0 rgba(0, 0, 0, 0.12)"
    override var blockBorder = "1px solid ${ZkColors.Zakadabar.gray3}"
    override var blockBackgroundColor = ZkColors.Zakadabar.gray0

    val background = ZkColors.white
    val foreground = ZkColors.Zakadabar.gray8

    val success = ZkColors.Zakadabar.navGreen
    val info = ZkColors.Zakadabar.navBlue
    val warning = ZkColors.Zakadabar.navOrange
    val error = ZkColors.Zakadabar.navRed

    override var dock = ZkDockTheme()

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
            thumb = textColor.alpha(0.5),
            track = backgroundColor
        )
    }

    override var tabContainer = ZkTabContainerTheme()
    override var table = ZkTableTheme()

    override var titleBar by after {
        ZkTitleBarTheme(
            appTitleBarHeight = "44px",
            localTitleBarHeight = "44px",
            appHandleBackground = ZkColors.Zakadabar.gray1,
            appHandleText = textColor,
            appHandleBorder = border,
            titleBarBackground = ZkColors.Zakadabar.gray1,
            titleBarText = textColor,
            titleBarBorder = border
        )
    }

}