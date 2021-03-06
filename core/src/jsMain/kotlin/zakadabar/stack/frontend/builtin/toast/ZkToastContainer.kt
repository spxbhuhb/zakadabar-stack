/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.toast

import kotlinx.browser.document
import zakadabar.stack.frontend.builtin.ZkElement

/**
 * Contains [ZkToast]s and shows them over the normal content.
 */
class ZkToastContainer : ZkElement() {

    override fun onCreate() {
        className = ZkToastStyles.toastContainer
        document.body?.appendChild(this.element)
    }

}