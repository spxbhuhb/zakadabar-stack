/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.resources

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

interface ZkTheme {

    val name: String

    var button: ZkButtonTheme
    var color: ZkColorTheme
    var dock: ZkDockTheme
    var font: ZkFontTheme
    var form: ZkFormTheme
    var icon: ZkIconTheme
    var layout: ZkLayoutTheme
    var modal: ZkModalTheme
    var note: ZkNoteTheme
    var scrollBar: ZkScrollBarTheme
    var sideBar: ZkSideBarTheme
    var tabContainer: ZkTabContainerTheme
    var table: ZkTableTheme
    var titleBar: ZkTitleBarTheme
    var toast: ZkToastTheme

}