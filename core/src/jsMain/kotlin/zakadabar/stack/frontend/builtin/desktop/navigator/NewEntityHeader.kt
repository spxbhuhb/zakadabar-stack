/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.navigator

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.Header
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.elements.ZkElement

class NewEntityHeader(
    private val newEntity: NewEntity
) : Header(
    title = t("create")
) {

    private val repeatIcon = Icons.repeat.complex16(::onRepeat)

    override fun init(): ZkElement {
        super.init()

        toolElement += repeatIcon.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.helpOutline.simple16.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.close.complex16(::onCancel).withClass(headerClasses.extensionIcon16)

        return this
    }

    private fun onCancel() {
        newEntity.close()
    }

    private fun onRepeat() {
        newEntity.repeat = ! newEntity.repeat
        if (newEntity.repeat) {
            repeatIcon.element.classList.add(headerClasses.activeExtensionIcon)
        } else {
            repeatIcon.element.classList.remove(headerClasses.activeExtensionIcon)
        }
    }

}