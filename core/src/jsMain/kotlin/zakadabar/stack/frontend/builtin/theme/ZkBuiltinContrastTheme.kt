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

open class ZkBuiltinContrastTheme : ZkTheme {

    override val name = "default-contrast"

    val success = ZkColors.Contrast.high
    val info = ZkColors.Contrast.high
    val warning = ZkColors.Contrast.high
    val error = ZkColors.Contrast.high

    val background = ZkColors.black
    val foreground = ZkColors.Contrast.high
    val border = "1px solid $foreground"

    override var button = ZkButtonTheme(
        iconFill = ZkColors.Contrast.high
    )

    override var color = ZkColorTheme(
        background = background,
        hoverBackground = background,
        foreground = foreground,
        hoverForeground = foreground,
        success = success,
        info = info,
        warning = warning,
        error = error,
        border = border
    )

    override var dock = ZkDockTheme()
    override var font = ZkFontTheme()
    override var form = ZkFormTheme()
    override var icon = ZkIconTheme()

    override var layout = ZkLayoutTheme(
        defaultBackground = background,
        defaultForeground = foreground
    )

    override var modal = ZkModalTheme()

    override var scrollBar = ZkScrollBarTheme(
        width = 10,
        height = 10,
        background = background,
        foreground = foreground
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