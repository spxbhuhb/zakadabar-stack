/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.frontend.builtin.desktop

import kotlinx.atomicfu.atomic
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.FrontendContext.entitySupports
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationEvent
import zakadabar.stack.frontend.builtin.util.Slider
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID

/**
 * A desktop center component that show a navigator and views for entities
 * with [FrontendEntitySupport][zakadabar.stack.frontend.extend.FrontendEntitySupport].
 *
 * On [GlobalNavigationEvent] gets the dto and then replaces the main with
 * one that can display the given dto.
 */
@PublicApi
open class DesktopCenter : ComplexElement() {

    companion object : ViewContract() {

        override val uuid = UUID("042098f3-9bd9-4850-875f-2a950c1ecc97")

        override val target = Desktop.center

        override fun newInstance() = DesktopCenter()

        val navigation = UUID("2bd7cff5-604a-4f0f-9912-cd5869c347e4")

        val regex = Regex("/api/${Stack.shid}/entities((/([0-9]*))(/([a-z]*))*)*")

    }

    private var revision = atomic(0)

    private val navigationInstance = FrontendContext.newInstance(navigation, ComplexElement::class)
    private var mainInstance: ComplexElement? = null

    override fun init(): ComplexElement {
        super.init()

        className = desktopClasses.center

        val slider = if (navigationInstance == null) {
            null
        } else {
            Slider(this, navigationInstance, minRemaining = 200.0).withClass(coreClasses.verticalSlider)
        }

        mainInstance = getMainInstance(null, "read")

        this += navigationInstance
        this += slider
        this += mainInstance

        on(GlobalNavigationEvent::class, ::onGlobalNavigationEvent)

        return this
    }

    private fun onGlobalNavigationEvent(message: GlobalNavigationEvent) {

        val match = regex.matchEntire(message.location) ?: return

        val eventRevision = revision.incrementAndGet()

        launch {
            val entityId = match.groupValues[3].toLongOrNull()
            val view = if (match.groupValues.size > 5) match.groupValues[5] else ""

            val dto = if (entityId == null) null else EntityCache.get(entityId)

            // entity load took too long, the user clicked to somewhere else
            if (eventRevision != revision.value) return@launch

            this -= mainInstance

            mainInstance = getMainInstance(dto, view)

            this += mainInstance
        }

    }

    open fun getMainInstance(dto: EntityDto?, view: String): ComplexElement? {
        if (dto == null) return null
        val entitySupport = entitySupports[dto.type] ?: return null

        return when (view) {
            "", "read" -> entitySupport.readView?.newElement(dto)
            "edit" -> entitySupport.editView?.newElement(dto)
            "preview" -> entitySupport.preView?.newElement(dto)
            "nav" -> entitySupport.navView?.newElement(dto)
            "new" -> entitySupport.newView?.newElement(dto)
            "remove" -> entitySupport.removeView?.newElement(dto)
            else -> null
        }
    }

}