/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import org.w3c.dom.set
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Base class for layouts of the application. Typically there are only
 * a few different layouts like full screen, with side bar, for admins, etc.
 *
 * Layouts are initialized when they are first used. Initialization:
 *
 * * calls [init]
 * * adds the [element] to the document body
 *
 * When the URL changes, the [Application]:
 *
 * * calls [AppRouting.onNavStateChange],which:
 *     * when the state points to a [Crud], calls [Crud.route]
 *     * when not a crud, calls [AppRouting.route]
 *     * after the route call returns:
 *     * if value of [AppRouting.layout]
 *         * is the same as [AppRouting.activeLayout], calls [resume]
 *         * is different than the active layout
 *             * calls [pause] of the old layout
 *             * calls [resume] of the new layout
 *             * sets [AppRouting.activeLayout] to the new layout
 */
abstract class AppLayout(val name: String) : ZkElement() {

    init {
        element.dataset["zk-layout-name"] = name
        document.body?.appendChild(element)
        hide()
    }

    /**
     * Resume the layout:
     *
     * * update permanent elements this layout uses (if necessary)
     * * resume automatic refresh processes (if necessary)
     * * add the element to the layout and/or replace the old one
     * * call [show] to show the HTML element of the layout
     *
     * @param  state  The navigation state to resume.
     */
    abstract fun resume(state: NavState, target: ZkElement)

    /**
     * Resume the layout:
     *
     * * pause automatic refresh processes (if necessary)
     * * remove the element shown by the layout
     * * call [hide] to hide the HTML element
     */
    abstract fun pause()

}