/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser

import zakadabar.core.browser.button.zkButtonStyles
import zakadabar.core.browser.field.zkFieldStyles
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.sidebar.zkSideBarStyles
import zakadabar.core.browser.tabcontainer.zkTabContainerStyles
import zakadabar.core.browser.table.zkTableStyles
import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.browser.toast.zkToastStyles
import zakadabar.softui.browser.theme.styles.*

fun install() {
    zkFormStyles = FormStyles()
    zkTitleBarStyles = TitleBarStyles()
    zkTabContainerStyles = TabContainerStyles()
    zkButtonStyles = ButtonStyles()
    zkSideBarStyles = SideBarStyles()
    zkPageStyles = PageStyles()
    zkTableStyles = TableStyles()
    zkToastStyles = ToastStyles()
    zkFieldStyles = FieldStyles()
}