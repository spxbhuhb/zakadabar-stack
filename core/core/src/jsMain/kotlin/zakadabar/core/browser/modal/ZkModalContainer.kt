/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.modal

import kotlinx.browser.document
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState

/**
 * Contains modal windows and shows them over the normal content.
 *
 * **NOTE** This class overrides mechanics defined by [ZkElement]. Pay attention
 * if you try to use it directly.
 *
 * TODO This works, but it feels a bit hackish. I should spend some time on the exact mechanism of multi modals.
 */
class ZkModalContainer : ZkElement() {

    val layers = mutableListOf<Layer>()

    override fun onCreate() {
        document.body?.appendChild(this.element)
        hide()
    }

    /**
     * Override of basic ZkElement.plusAssign. This function
     * overrides the Z-index of modals to ensure that they
     * are properly stacked.
     */
    override operator fun plusAssign(child: ZkElement?) {
        if (child == null) return

        val layer = Layer(child, document.createElement("div") as HTMLElement)

        with(layer.background) {
            css(zkModalStyles.modalContainer)
            appendChild(child.element)
            style.zIndex = (1900 + childElements.size).toString()
        }

        layers += layer

        syncChildrenState(child)

        this.element.appendChild(layer.background)
    }

    override operator fun minusAssign(child: ZkElement?) {
        if (child == null) return

        child.onPause()

        child.lifeCycleState = ZkElementState.Created

        layers.filter { it.child == child }.forEach {
            it.background.clear()
            it.background.remove()
        }

        layers.removeAll { it.child == child }

        if (layers.isEmpty()) hide()
    }

    class Layer(
        val child: ZkElement,
        val background: HTMLElement
    )


}