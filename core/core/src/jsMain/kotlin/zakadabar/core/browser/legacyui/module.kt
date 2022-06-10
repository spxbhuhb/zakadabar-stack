/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.legacyui

import zakadabar.core.browser.button.ZkButtonStyles
import zakadabar.core.browser.button.zkButtonStyles
import zakadabar.core.browser.counterbar.ZkCounterBarStyles
import zakadabar.core.browser.counterbar.zkCounterBarStyles
import zakadabar.core.browser.field.ZkFieldStyles
import zakadabar.core.browser.field.zkFieldStyles
import zakadabar.core.browser.form.ZkFormStyles
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.modal.ZkModalStyles
import zakadabar.core.browser.modal.zkModalStyles
import zakadabar.core.browser.note.ZkNoteStyles
import zakadabar.core.browser.note.zkNoteStyles
import zakadabar.core.browser.page.ZkPageStyles
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.sidebar.ZkSideBarStyles
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.tabcontainer.ZkTabContainerStyles
import zakadabar.core.browser.tabcontainer.zkTabContainerStyles
import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.browser.table.zkTableStyles
import zakadabar.core.browser.titlebar.ZkTitleBarStyles
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.browser.toast.ZkToastStyles
import zakadabar.core.browser.toast.zkToastStyles

fun install() {
    zkFormStyles = ZkFormStyles()
    zkTitleBarStyles = ZkTitleBarStyles()
    zkTabContainerStyles = ZkTabContainerStyles()
    zkButtonStyles = ZkButtonStyles()
    zkSideBarStyles = ZkSideBarStyles()
    zkPageStyles = ZkPageStyles()
    zkTableStyles = ZkTableStyles()
    zkToastStyles = ZkToastStyles()
    zkFieldStyles = ZkFieldStyles()
    zkNoteStyles = ZkNoteStyles()
    zkModalStyles = ZkModalStyles()
    zkCounterBarStyles = ZkCounterBarStyles()
}