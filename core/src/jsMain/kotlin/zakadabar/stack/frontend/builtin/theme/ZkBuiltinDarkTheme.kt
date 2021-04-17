/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.builtin.button.ZkButtonTheme
import zakadabar.stack.frontend.builtin.dock.ZkDockTheme
import zakadabar.stack.frontend.builtin.form.ZkFormTheme
import zakadabar.stack.frontend.builtin.icon.ZkIconTheme
import zakadabar.stack.frontend.builtin.layout.ZkLayoutTheme
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainerTheme
import zakadabar.stack.frontend.builtin.misc.ZkFontTheme
import zakadabar.stack.frontend.builtin.modal.ZkModalTheme
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarTheme
import zakadabar.stack.frontend.builtin.table.ZkTableTheme
import zakadabar.stack.frontend.builtin.toast.ZkToastTheme
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme

open class ZkBuiltinDarkTheme : ZkTheme {
    override var button = ZkButtonTheme(
        background = ZkColors.Design.navBlue
    )
    override var dock = ZkDockTheme()
    override var font = ZkFontTheme(
        family = "'IBM Plex Sans', sans-serif"
    )
    override var form = ZkFormTheme()
    override var icon = ZkIconTheme()
    override var layout = ZkLayoutTheme(
        defaultForeground = ZkColors.Design.gray2,
        defaultBackground = ZkColors.Design.gray9
    )
    override var modal = ZkModalTheme()
    override var sidebar = ZkSideBarTheme()
    override var tabContainer = ZkTabContainerTheme()
    override var table = ZkTableTheme()
    override var toast = ZkToastTheme()
}