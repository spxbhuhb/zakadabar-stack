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

open class ZkBuiltinDarkTheme : ZkTheme {

    companion object {
        const val NAME = "zakadabar.stack.theme.dark"
    }

    override val name = NAME

    override var fontFamily = "'Lato', sans-serif"
    override var fontSize: String = "16px"
    override var fontWeight: String = "300"

    override var backgroundColor = ZkColors.Design.gray7
    override var textColor = ZkColors.Design.gray2

    override var hoverBackgroundColor = "rgba(255,255,255,0.2)"
    override var hoverTextColor = ZkColors.Design.gray2

    override var primaryColor = ZkColors.Design.navPurple
    override var primaryPair = ZkColors.white
    override var secondaryColor = ZkColors.Design.gray3
    override var secondaryPair = ZkColors.Design.gray8
    override var successColor = ZkColors.Design.navGreen
    override var successPair = ZkColors.Design.gray8
    override var warningColor = ZkColors.Design.navOrange
    override var warningPair = ZkColors.white
    override var dangerColor = ZkColors.Design.navRed
    override var dangerPair = ZkColors.white
    override var infoColor = ZkColors.Design.navBlue
    override var infoPair = ZkColors.white
    override var disabledColor = ZkColors.Design.gray4
    override var disabledPair = ZkColors.Design.gray7

    override var inputTextColor by after { textColor }
    override var inputBackgroundColor = ZkColors.Design.gray6

    override var disabledInputColor by after { disabledColor }
    override var disabledInputPair by after { disabledPair }

    override var borderColor = "transparent" // ZkColors.Gray.c600

    override var border by after { "1px solid $borderColor" }
    override var cornerRadius = 2
    override var spacingStep = 20

    val success = ZkColors.Design.navGreen
    val info = ZkColors.Design.navBlue
    val warning = ZkColors.Design.navOrange
    val error = ZkColors.Design.navRed

    val background = ZkColors.Design.gray9
    val foreground = ZkColors.Design.gray2

    override var dock = ZkDockTheme()

    override var form by after {
        ZkFormTheme(
            rowHeight = 38,
            disabledBackground = ZkColors.BlueGray.c50,
            labelBackground = backgroundColor,
            valueBackground = ZkColors.white,
            invalidBackground = dangerColor.alpha(0.5)
        )
    }

    override var layout = ZkLayoutTheme(
        defaultForeground = foreground,
        defaultBackground = background
    )

    override var note = ZkNoteTheme(
        background = ZkColors.Gray.c800
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
            appHandleBackground = ZkColors.Design.gray8,
            appHandleText = textColor,
            appHandleBorder = border,
            titleBarBackground = ZkColors.Design.gray8,
            titleBarText = textColor,
            titleBarBorder = border
        )
    }

}