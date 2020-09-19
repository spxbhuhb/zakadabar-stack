/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import kotlinx.atomicfu.atomic
import zakadabar.stack.Stack
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.builtin.desktop.messages.GlobalNavigationEvent
import zakadabar.stack.frontend.builtin.desktop.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.util.Slider
import zakadabar.stack.frontend.comm.rest.EntityCache
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

/**
 * A desktop center component that show a navigator and views for entities
 * with [FrontendEntitySupport][zakadabar.stack.frontend.extend.DtoFrontend].
 *
 * On [GlobalNavigationEvent] gets the dto and then replaces the main with
 * one that can display the given dto.
 */
@PublicApi
open class DesktopCenter(
    private val navigationInstance: ComplexElement? = EntityNavigator()
) : ComplexElement() {

    companion object {
        val regex = Regex("/api/${Stack.shid}/entities((/([0-9]*))(/([a-z]*))*)*")
    }

    private var revision = atomic(0)

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

            val dto = if (entityId == null) null else EntityCache.read(entityId)

            // entity load took too long, the user clicked to somewhere else
            if (eventRevision != revision.value) return@launch

            this -= mainInstance

            mainInstance = getMainInstance(null, view)

            this += mainInstance
        }

    }

    open fun getMainInstance(id: Long?, view: String): ComplexElement? {
        return null
//        if (dto == null) return null
//
//        @Suppress("UNCHECKED_CAST")
//        val dtoFrontend = dtoFrontends[dto.entityType] ?: return null
//
//        return when (view) {
//            "create" -> dtoFrontend.createView()
//            "", "read" -> dtoFrontend.readView(dto.id)
//            "update" -> dtoFrontend.updateView(dto.id)
//            "delete" -> dtoFrontend.deleteView(dto.id)
//            else -> null
//        }
    }

}