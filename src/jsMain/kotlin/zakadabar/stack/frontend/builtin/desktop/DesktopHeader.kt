/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import kotlinx.dom.clear
import org.w3c.dom.events.Event
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext.dispatcher
import zakadabar.stack.frontend.builtin.desktop.DesktopCenter.Companion.regex
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationEvent
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationRequest
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.getElementId
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

@PublicApi
class DesktopHeader : ComplexElement() {

    private val idPrefix = element.id + "-item-"

    private val path = Path()

    override fun init(): DesktopHeader {
        super.init()

        this cssClass desktopClasses.header build {

            + Icons.menu.simple(14) cssClass desktopClasses.headerIcon

            + Icons.home.complex16 cssClass headerClasses.innerIcon build {
                on("click", ::onHomeClick)
                on("dblclick", ::onHomeClick)
            }

            + path
        }

        on(GlobalNavigationEvent::class, ::onGlobalNavigationEvent)

        return this
    }

    private fun onGlobalNavigationEvent(message: GlobalNavigationEvent) {

        val match = regex.matchEntire(message.location) ?: return

        val id = match.groupValues[3].toLongOrNull()

        launch {
            path.update(if (id == null) null else EntityCache.read(id))
        }
    }

    private fun onHomeClick(@Suppress("UNUSED_PARAMETER") event: Event) =
        dispatcher.postSync { GlobalNavigationRequest(null) }

    class PathItem(
        val id: Long,
        val name: String
    )

    fun PathItem.toHtml(): String {
        return """<a id="$idPrefix$id" class="${headerClasses.pathItem}">$name</a>"""
    }

    inner class Path : ComplexElement() {

        override fun init(): ComplexElement {
            className = headerClasses.path

            on("click", ::onClick)
            on("dblclick", ::onClick)

            return this
        }

        suspend fun update(dto: EntityRecordDto?) {

            var current = dto
            val items = mutableListOf<PathItem>()

            while (current != null) {
                items += PathItem(current.id, current.name)
                val parentId = current.parentId ?: break
                current = EntityRecordDto.read(parentId)
            }

            if (items.isNotEmpty()) {
                items.reverse()
                val separator = "&nbsp;/&nbsp;"
                element.innerHTML = separator + items.joinToString(separator) { it.toHtml() }
            } else {
                element.clear()
            }
        }

        private fun onClick(event: Event) {
            val entityId = getElementId(event, idPrefix)?.second?.toLong() ?: return
            dispatcher.postSync { GlobalNavigationRequest(entityId) }
        }

    }
}