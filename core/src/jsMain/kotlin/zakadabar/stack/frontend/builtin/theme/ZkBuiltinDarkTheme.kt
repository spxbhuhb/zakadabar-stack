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
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarTheme
import zakadabar.stack.frontend.builtin.toast.ZkToastTheme
import zakadabar.stack.frontend.resources.ZkColorTheme
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme

open class ZkBuiltinDarkTheme : ZkTheme {

    override val name = "default-dark"

    val success = ZkColors.Design.navGreen
    val info = ZkColors.Design.navBlue
    val warning = ZkColors.Design.navOrange
    val error = ZkColors.Design.navRed

    val background = ZkColors.Design.gray9
    val foreground = ZkColors.Design.gray2

    val borderColor = ZkColors.Gray.c600
    val border = "1px solid $borderColor"

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