/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.button.ZkButtonTheme
import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
import zakadabar.stack.frontend.builtin.form.ZkFormTheme
import zakadabar.stack.frontend.builtin.icon.ZkIconTheme
import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.layout.ZkScrollBarTheme
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainerTheme
import zakadabar.stack.frontend.builtin.misc.ZkFontTheme
import zakadabar.stack.frontend.builtin.modal.ZkModalTheme
import zakadabar.stack.frontend.builtin.note.ZkNoteTheme
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarTheme
import zakadabar.stack.frontend.builtin.toast.ZkToastTheme
import zakadabar.stack.frontend.resources.ZkColorTheme
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme

open class ZkBuiltinDarkTheme : ZkTheme {

    companion object {
        const val NAME = "zakadabar.stack.theme.dark"
    }

    override val name = NAME

    override val backgroundColor = ZkColors.Design.gray9
    override val textColor = ZkColors.Design.gray2

    override val primaryColor = ZkColors.Design.navGreen
    override val primaryPair = ZkColors.Design.gray8
    override val secondaryColor = ZkColors.Design.gray3
    override val secondaryPair = ZkColors.white
    override val successColor = ZkColors.Design.navGreen
    override val successPair = ZkColors.Design.gray8
    override val warningColor = ZkColors.Design.navOrange
    override val warningPair = ZkColors.white
    override val dangerColor = ZkColors.Design.navRed
    override val dangerPair = ZkColors.white
    override val infoColor = ZkColors.Design.navBlue
    override val infoPair = ZkColors.white
    override val disabledColor = ZkColors.Design.gray3
    override val disabledPair = ZkColors.black

    override val borderColor = ZkColors.Gray.c600

    override val border = "1px solid ${ZkColors.Gray.c600}"
    override val cornerRadius = 2

    val success = ZkColors.Design.navGreen
    val info = ZkColors.Design.navBlue
    val warning = ZkColors.Design.navOrange
    val error = ZkColors.Design.navRed

    val background = ZkColors.Design.gray9
    val foreground = ZkColors.Design.gray2

    override var button = ZkButtonTheme(
        background = ZkColors.Design.navBlue,
        iconFill = foreground
    )

    override var color = ZkColorTheme(
        background = background,
        hoverBackground = "rgba(255,255,255,0.2)",
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
    override var icon = ZkIconTheme()

    override var layout = ZkLayoutTheme(
        defaultForeground = foreground,
        defaultBackground = background
    )

    override var modal = ZkModalTheme()

    override var note = ZkNoteTheme(
        background = ZkColors.Gray.c800
    )

    override var scrollBar = ZkScrollBarTheme(
        width = 12,
        height = 12,
        background = foreground,
        foreground = "${background}80"
    )

    override var sideBar = ZkSideBarTheme(
        border = border
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