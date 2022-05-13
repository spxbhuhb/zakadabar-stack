/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.toast.ZkToastStyles
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

var toastStyles by cssStyleSheet(ToastStyles())

open class ToastStyles : ZkToastStyles() {

    /**
     * For the close icon.
     */
    override val closeIcon by cssClass {
        fill = theme.textColor
        marginRight = 6.px

    }

}