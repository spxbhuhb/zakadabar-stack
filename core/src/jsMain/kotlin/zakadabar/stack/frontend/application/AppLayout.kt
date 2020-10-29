/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import org.w3c.dom.set
import zakadabar.stack.frontend.application.navigation.NavState
import zakadabar.stack.frontend.application.navigation.NavTarget
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Base class for all layouts of the application. Typically there are only
 * a few different layouts like full screen, with side bar, for admins, etc.
 *
 * Layouts are initialized as soon as they are added to the [Application].
 * Initialization:
 *
 * * calls [init]
 * * adds the [element] to the document body
 *
 * When the URL changes, the navigation:
 *
 * * finds the [NavTarget] that handles the new URL
 * * when the layout of that [NavTarget]
 *    * is the same as the active layout calls [resume]
 *    * is different than the active layout
 *       * calls [pause] of the old layout
 *       * calls [resume] of the new layout
 *
 */
abstract class AppLayout(val name: String) : ZkElement() {

    init {
        element.dataset["zk-layout-name"] = name
        document.body?.appendChild(element)
        hide()
    }

    /**
     * Navigation targets bound to this layout.
     */
    val navTargets = mutableListOf<NavTarget>()

    /**
     * Resume the layout:
     *
     * * update non-target specific data and elements this layout uses (if necessary)
     * * resume automatic refresh processes (if necessary)
     * * get the element to show by calling [NavState.target].element
     * * add the element to the layout and/or replace the old one
     * * call [show] to show the HTML element
     *
     * @param  state  The navigation state to resume.
     */
    abstract fun resume(state: NavState)

    /**
     * Resume the layout:
     *
     * * pause automatic refresh processes (if necessary)
     * * remove the element shown by the layout
     * * call [hide] to hide the HTML element
     */
    abstract fun pause()

}