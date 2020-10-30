/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.navigator

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.DataTransfer
import org.w3c.dom.events.Event
import zakadabar.entity.browser.frontend.util.EntityDrop
import zakadabar.stack.data.builtin.FolderDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.messages.EntityAdded
import zakadabar.stack.frontend.builtin.desktop.messages.EntityChildrenLoaded
import zakadabar.stack.frontend.builtin.desktop.messages.EntityRemoved
import zakadabar.stack.frontend.builtin.desktop.messages.EntityUpdated
import zakadabar.stack.frontend.builtin.desktop.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.util.droparea.DropArea
import zakadabar.stack.frontend.builtin.util.status.Status
import zakadabar.stack.frontend.builtin.util.status.StatusInfo
import zakadabar.stack.frontend.builtin.util.status.StatusMessages
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.getElementId
import zakadabar.stack.frontend.util.launch

class EntityNavigator : ZkElement() {

    internal val header = EntityNavigatorHeader(this)

    private val statusInfo = StatusInfo(StatusMessages(emptyMessage = t("emptyFolder")))
    internal val items = ZkElement()
    private val dropArea = DropArea(::onDrop)

    private val idPrefix = "${element.id}-item-"

    internal var currentEntityId: Long? = null
    private var currentEntityDto: EntityRecordDto? = null
    private var selectedEntityId: Long? = null

    override fun init(): ZkElement {
        super.init()

        this cssClass navigatorClasses.navigator build {

            + header

            + element() cssClass navigatorClasses.content build {

                + statusInfo.update(Status.Loading)
                + (items cssClass navigatorClasses.items).hide()
                + dropArea

                on("click", ::onClick)
                on("dblclick", ::onClick)
            }
        }

        on(window, Navigation.EVENT, ::onNavigation)

        return this

    }

    private fun onEntityChildrenLoaded(message: EntityChildrenLoaded) {
        if (message.entityId != currentEntityId) return
        if (message.error != null) {
            statusInfo.update(Status.CommError, message.error.toString()).show()
        } else {
            launch { render() }
        }
    }

    private fun onNavigation() {
        val state = Navigation.state

        launch {
            render() // FIXME this is not properly synchronized
        }
    }

    private fun onEntityAdded(message: EntityAdded) = refresh(message.dto.parentId)

    private fun onEntityRemoved(message: EntityRemoved) = refresh(message.dto.parentId)

    private fun onEntityUpdated(message: EntityUpdated) = refresh(message.dto.parentId)

    private fun refresh(entityId: Long?) {
        if (entityId != currentEntityId) return

        EntityCache.launchGetChildren(currentEntityId) { children, error ->
            dispatcher.postSync { EntityChildrenLoaded(currentEntityId, children, error) }
        }

    }

    // ---- Event handling -----------------------------------------------

    private fun onClick(event: Event) {
        val entityId = getEntityId(event) ?: return
//        Navigation.changeLocation(Navigation.READ) { EntityRecordDto.read(entityId) }
    }

    private fun getEntityId(event: Event): Long? {
        val element = getElementId(event, idPrefix)

        if (selectedEntityId != null) {
            document.getElementById(idPrefix + selectedEntityId)?.classList?.remove(navigatorClasses.selectedListItem)
            selectedEntityId = null
        }

        if (element == null) return null

        val (current, id) = element
        current.classList.add(navigatorClasses.selectedListItem)
        selectedEntityId = id.toLong()

        return selectedEntityId
    }

    // ---- Rendering ----------------------------------------------------

    private suspend fun render() {
        currentEntityDto = currentEntityId?.let { EntityCache.read(it) }

        val children = EntityCache.childrenOf(currentEntityId)

        if (children.isEmpty()) {
            statusInfo.update(Status.Empty).show()
            items.clearChildren().hide()
            return
        }

        statusInfo.hide()
        items.clearChildren().show()

        selectedEntityId = null

        val sortedChildren = children.toMutableList()
        sortedChildren.sortBy { it.name }
        sortedChildren.sortBy { if (it.entityType == FolderDto.type) 0 else 1 }

        sortedChildren.forEach {
            items += EntityNavigatorItem(idPrefix, it)
        }
    }

    private fun onDrop(dataTransfer: DataTransfer) =
        EntityDrop(currentEntityId).process(dataTransfer)

}