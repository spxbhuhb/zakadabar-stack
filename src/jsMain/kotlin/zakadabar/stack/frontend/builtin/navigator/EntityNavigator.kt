/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigator

import kotlinx.browser.document
import org.w3c.dom.DataTransfer
import org.w3c.dom.events.Event
import zakadabar.entity.browser.frontend.util.EntityDrop
import zakadabar.stack.data.FolderDto
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.DesktopCenter
import zakadabar.stack.frontend.builtin.desktop.DesktopCenter.Companion.regex
import zakadabar.stack.frontend.builtin.desktop.messages.EntityChildrenLoaded
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationEvent
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationRequest
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.navigator.messages.PreviewEntityIntent
import zakadabar.stack.frontend.builtin.util.droparea.DropArea
import zakadabar.stack.frontend.builtin.util.status.Status
import zakadabar.stack.frontend.builtin.util.status.StatusInfo
import zakadabar.stack.frontend.builtin.util.status.StatusMessages
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.frontend.util.getElementId
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.UUID

class EntityNavigator : ComplexElement() {

    companion object : ViewContract() {

        override var uuid = UUID("1d43b96a-485d-49b7-8419-025e8dab832e")

        override var target = DesktopCenter.navigation

        val newEntity = UUID("9de640f7-2c1e-4862-8672-fc9b2e52c287")

        override fun newInstance() = EntityNavigator()
    }

    internal val header = EntityNavigatorHeader(this)

    private val statusInfo = StatusInfo(StatusMessages(emptyMessage = t("emptyFolder")))
    internal val items = ComplexElement()
    private val dropArea = DropArea(::onDrop)

    private val idPrefix = "${element.id}-item-"

    internal var currentEntityId: Long? = null
    private var selectedEntityId: Long? = null

    override fun init(): ComplexElement {
        super.init()

        this cssClass navigatorClasses.navigator build {

            + header

            + complex() cssClass navigatorClasses.content build {

                + statusInfo.update(Status.Loading)
                + (items cssClass navigatorClasses.items).hide()
                + dropArea

                on("click", ::onClick)
                on("dblclick", ::onDoubleClick)
            }
        }

        on(GlobalNavigationRequest::class, ::onGlobalNavigationRequest)
        on(GlobalNavigationEvent::class, ::onGlobalNavigationEvent)
        on(EntityChildrenLoaded::class, ::onEntityChildrenLoaded)

        return this

    }

    // ---- Message handling (except new entity intents) ------------------

    private fun onEntityChildrenLoaded(message: EntityChildrenLoaded) {
        if (message.entityId != currentEntityId) return
        if (message.status != null) {
            statusInfo.update(Status.CommError, message.status.toString()).show()
        } else {
            launch { render() }
        }
    }

    private fun onGlobalNavigationRequest(message: GlobalNavigationRequest) {

        val match = regex.matchEntire(message.location) ?: return

        EntityCache.launchGetChildren(match.groupValues[3].toLongOrNull()) // this will send an EntityChildrenLoaded

        dispatcher.postSync { GlobalNavigationEvent(message.location) }
    }

    private fun onGlobalNavigationEvent(message: GlobalNavigationEvent) {

        val match = regex.matchEntire(message.location) ?: return

        currentEntityId = match.groupValues[3].toLongOrNull()

    }

    // ---- Event handling -----------------------------------------------

    private fun onClick(event: Event) {
        val entityId = getEntityId(event) ?: return
        dispatcher.postSync { PreviewEntityIntent(entityId) }
    }

    private fun onDoubleClick(event: Event) {
        val entityId = getEntityId(event) ?: return
        dispatcher.postSync { GlobalNavigationRequest(entityId) }
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
        val children = EntityCache.getChildrenOf(currentEntityId)

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
        sortedChildren.sortBy { if (it.type == FolderDto.type) 0 else 1 }

        sortedChildren.forEach {
            items += EntityNavigatorItem(idPrefix, it)
        }
    }

    private fun onDrop(dataTransfer: DataTransfer) =
        EntityDrop(currentEntityId).process(dataTransfer)

}