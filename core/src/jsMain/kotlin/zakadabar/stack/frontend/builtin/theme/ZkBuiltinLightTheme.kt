/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.button.ZkButtonTheme
import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
import zakadabar.stack.frontend.builtin.form.ZkFormTheme
import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.layout.ZkScrollBarTheme
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainerTheme
import zakadabar.stack.frontend.builtin.misc.ZkFontTheme
import zakadabar.stack.frontend.builtin.modal.ZkModalTheme
import zakadabar.stack.frontend.builtin.note.ZkNoteTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarTheme
import zakadabar.stack.frontend.builtin.toast.ZkToastTheme
import zakadabar.stack.frontend.resources.ZkColorTheme
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme

open class ZkBuiltinLightTheme : ZkTheme {

    companion object {
        const val NAME = "zakadabar.stack.theme.light"
    }

    override val name = NAME

    override val backgroundColor = ZkColors.white
    override val textColor = ZkColors.Design.gray8

    override val hoverBackgroundColor = "rgba(0,0,0,0.1)"
    override val hoverTextColor = ZkColors.Design.gray8

    override val primaryColor = ZkColors.Design.navPurple
    override val primaryPair = ZkColors.white
    override val secondaryColor = ZkColors.Design.gray5
    override val secondaryPair = ZkColors.white
    override val successColor = ZkColors.Design.navGreen
    override val successPair = ZkColors.white
    override val warningColor = ZkColors.Design.navOrange
    override val warningPair = ZkColors.white
    override val dangerColor = ZkColors.Design.navRed
    override val dangerPair = ZkColors.white
    override val infoColor = ZkColors.Design.navBlue
    override val infoPair = ZkColors.white
    override val disabledColor = ZkColors.Design.gray2
    override val disabledPair = ZkColors.Design.gray6

    override val borderColor = "${ZkColors.Gray.c600}80"

    override val border = "1px solid ${ZkColors.Gray.c600}80"
    override val cornerRadius = 2
    override val spacingStep = 20

    val background = ZkColors.white
    val foreground = ZkColors.Design.gray8

    val success = ZkColors.Design.navGreen
    val info = ZkColors.Design.navBlue
    val warning = ZkColors.Design.navOrange
    val error = ZkColors.Design.navRed

    override var button = ZkButtonTheme(
        background = ZkColors.Design.navBlue,
        foreground = ZkColors.white,
        iconFill = ZkColors.white
    )

    override var color = ZkColorTheme(
        background = background,
        hoverBackground = "rgba(0,0,0,0.1)",
        foreground = foreground,
        hoverForeground = foreground,
        success = success,
        info = info,
        warning = warning,
        error = error,
        border = borderColor
    )

    override var dock = ZkDockTheme()

    override var font = ZkFontTheme(
        family = "'IBM Plex Sans', sans-serif"
    )

    override var form = ZkFormTheme()

    override var layout = ZkLayoutTheme(
        defaultForeground = foreground,
        defaultBackground = background
    )

    override var modal = ZkModalTheme(
        border = border
    )

    override var note = ZkNoteTheme(
        background = ZkColors.Gray.c50
    )

    override var scrollBar = ZkScrollBarTheme(
        width = 12,
        height = 12,
        background = "${foreground}80",
        foreground = background
    )

    override var tabContainer = ZkTabContainerTheme()
    override var table = ZkTableTheme()

    override var titleBar = ZkTitleBarTheme(
        appHandleBackground = background,
        appHandleForeground = foreground,
        appHandleBorder = border,
        titleBarBackground = background,
        titleBarForeground = foreground,
        titleBarBorder = border,
        height = "44px"
    )

    override var toast = ZkToastTheme(
        infoBackground = info,
        infoText = ZkColors.black,
        successBackground = success,
        successText = ZkColors.black,
        warningBackground = warning,
        warningText = ZkColors.black,
        errorBackground = error,
        errorText = ZkColors.black
    )
}