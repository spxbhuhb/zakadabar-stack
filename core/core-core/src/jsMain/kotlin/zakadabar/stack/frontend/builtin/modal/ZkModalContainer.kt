/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import kotlinx.browser.document
import zakadabar.stack.frontend.builtin.ZkElement

/**
 * Contains modal windows and shows them over the normal content.
 *
 * TODO handle more than one children at the same time by setting the zIndex properly (1900 + number of children)
 */
class ZkModalContainer : ZkElement() {

    override fun onCreate() {
        + zkModalStyles.modalContainer
        document.body?.appendChild(this.element)
        hide()
    }

}