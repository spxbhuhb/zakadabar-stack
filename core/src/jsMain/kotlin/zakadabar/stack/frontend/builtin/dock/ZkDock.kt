/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import kotlinx.browser.document
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Contains [ZkDockedElement]s and shows them over the normal content. The items
 * may be minimized, normal sized or maximized. Useful to pick out elements
 * from the normal document flow, mostly for editing. Check the cookbook for
 * examples.
 */
class ZkDock : ZkElement() {

    override fun init(): ZkDock {
        className = coreClasses.dock
        document.body?.appendChild(this.element)
        return this
    }

    /**
     * A modified version of [ZkElement.minusAssign] to remove a child.
     * This version checks the content field of [ZkDockedElement] children
     * and removes the [ZkDockedElement] if the content field contains the
     * element passed in the [child] parameter.
     */
    override operator fun minusAssign(child: ZkElement?) {

        if (child == null) return

        if (childElements.contains(child)) {
            childElements -= child
            child.cleanup()
            child.element.remove()
            return
        }

        for (candidate in childElements) {
            if (candidate !is ZkDockedElement) continue
            if (candidate.content != child) continue
            childElements -= candidate
            candidate.cleanup()
            candidate.element.remove()
            return
        }

    }
}