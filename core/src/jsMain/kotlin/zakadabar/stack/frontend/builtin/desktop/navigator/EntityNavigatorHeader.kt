/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.navigator

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.Header
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

class EntityNavigatorHeader(
    private val navigator: EntityNavigator
) : Header(
    title = t("navigation")
) {

    override fun init(): ZkElement {
        super.init()

        this.element.style.marginLeft = "10px"

        toolElement += Icons.filterAlt.simple16.withClass(headerClasses.extensionIcon16)
        toolElement += Icons.addBox.complex16(::onNewEntity).withClass(headerClasses.extensionIcon16)
        toolElement += Icons.helpOutline.simple16.withClass(headerClasses.extensionIcon16)

        return this
    }

    private fun onNewEntity() {
        // to prevent more new entity elements at the same time
        if (navigator.hasChildOf(NewEntity::class)) return

        launch {
            val parentId = navigator.currentEntityId
            val newEntity = NewEntity(navigator, if (parentId == null) null else EntityCache.read(parentId))
            navigator.insertAfter(newEntity, navigator.header)
            newEntity.focus()
        }
    }

}