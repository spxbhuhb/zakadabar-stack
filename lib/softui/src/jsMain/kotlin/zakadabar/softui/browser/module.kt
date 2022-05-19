/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser

import zakadabar.core.browser.button.zkButtonStyles
import zakadabar.core.browser.counterbar.zkCounterBarStyles
import zakadabar.core.browser.field.zkFieldStyles
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.modal.zkModalStyles
import zakadabar.core.browser.note.zkNoteStyles
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.tabcontainer.zkTabContainerStyles
import zakadabar.core.browser.table.zkTableStyles
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.browser.toast.zkToastStyles
import zakadabar.softui.browser.theme.styles.*

fun install() {
    zkFormStyles = SuiFormStyles()
    zkTitleBarStyles = SuiTitleBarStyles()
    zkTabContainerStyles = SuiTabContainerStyles()
    zkButtonStyles = SuiButtonStyles()
    zkSideBarStyles = SuiSideBarStyles()
    zkPageStyles = SuiPageStyles()
    zkTableStyles = SuiTableStyles()
    zkToastStyles = SuiToastStyles()
    zkFieldStyles = SuiFieldStyles()
    zkNoteStyles = SuiNoteStyles()
    zkModalStyles = SuiModalStyles()
    zkCounterBarStyles = SuiCounterBarStyles()
}