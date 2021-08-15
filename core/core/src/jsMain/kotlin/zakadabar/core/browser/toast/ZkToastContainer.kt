/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.toast

import kotlinx.browser.document
import zakadabar.core.browser.ZkElement

/**
 * Contains [ZkToast]s and shows them over the normal content.
 */
class ZkToastContainer : ZkElement() {

    override fun onCreate() {
        + zkToastStyles.appToastContainer
        document.body?.appendChild(this.element)
    }

}