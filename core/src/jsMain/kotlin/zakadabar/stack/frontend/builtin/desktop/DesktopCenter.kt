/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import kotlinx.atomicfu.atomic
import kotlinx.browser.window
import zakadabar.stack.frontend.FrontendContext.dtoFrontends
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.builtin.desktop.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.navigation.Navigation
import zakadabar.stack.frontend.builtin.navigation.NavigationState
import zakadabar.stack.frontend.builtin.util.Slider
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.util.PublicApi

/**
 * A desktop center component that show a navigator and views/pages.
 */
@PublicApi
open class DesktopCenter(
    private val navigationInstance: ComplexElement? = EntityNavigator()
) : ComplexElement() {

    private var revision = atomic(0)

    private var mainInstance: SimpleElement? = null

    override fun init(): ComplexElement {
        super.init()

        className = desktopClasses.center

        val slider = if (navigationInstance == null) {
            null
        } else {
            Slider(this, navigationInstance, minRemaining = 200.0).withClass(coreClasses.verticalSlider)
        }

        mainInstance = SimpleElement()

        this += navigationInstance
        this += slider
        this += mainInstance

        on(window, Navigation.EVENT, ::onNavigation)

        return this
    }

    open fun onNavigation() {

        val state = Navigation.state

        val eventRevision = revision.incrementAndGet()

        launch {
            val viewState = state.viewState ?: return@launch

            // entity load took too long, the user clicked to somewhere else
            if (eventRevision != revision.value) return@launch

            this -= mainInstance

            mainInstance = getMainInstance(viewState)

            this += mainInstance
        }

    }

    open fun getMainInstance(viewState: NavigationState.ViewState): ComplexElement? {
        if (viewState.localId == null) return null

        @Suppress("UNCHECKED_CAST")
        val dtoFrontend = dtoFrontends[viewState.dataType] ?: return null

        return when (viewState.viewName) {
            Navigation.CREATE -> dtoFrontend.createView()
            Navigation.READ -> dtoFrontend.readView()
            Navigation.UPDATE -> dtoFrontend.updateView()
            Navigation.DELETE -> dtoFrontend.deleteView()
            Navigation.ALL -> dtoFrontend.allView()
            else -> null
        }
    }

    fun switchMain(newMain: ComplexElement) {
        this -= mainInstance
        mainInstance = newMain
        this += mainInstance
    }
}