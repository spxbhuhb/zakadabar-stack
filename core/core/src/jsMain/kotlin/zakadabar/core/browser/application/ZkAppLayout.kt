/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.application

import kotlinx.browser.document
import org.w3c.dom.set
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.util.plusAssign

/**
 * Base class for layouts of the application. Typically there are only
 * a few different layouts like full screen, with side bar, for admins, etc.
 *
 * Layouts are initialized when they are first used. Initialization:
 *
 * * calls [onResume]
 * * adds the [element] to the document body
 *
 * When the URL changes, the [application]:
 *
 * * calls [ZkAppRouting.onNavStateChange],which:
 *     * when the state points to a [Crud], calls [Crud.route]
 *     * when not a crud, calls [AppRouting.route]
 *     * after the route call returns:
 *     * if value of [AppRouting.layout]
 *         * is the same as [AppRouting.activeLayout], calls [resume]
 *         * is different than the active layout
 *             * calls [pause] of the old layout
 *             * calls [resume] of the new layout
 *             * sets [AppRouting.activeLayout] to the new layout
 *
 * @property  contentContainer        container for [activeElement], never changes
 * @property  activeElement  the active element displayed
 */
abstract class ZkAppLayout(val name: String) : ZkElement() {

    protected var activeElement: ZkElement? = null
    protected var contentContainer = ZkElement()

    init {
        element.classList += zkLayoutStyles.layout
        element.classList += zkLayoutStyles.hidden

        element.dataset["zkLayoutName"] = name

        document.body?.appendChild(element)
    }

    override fun onCreate() {
        + contentContainer
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
    open fun resume(state: ZkNavState, target: ZkElement) {
        contentContainer -= activeElement

        onResume()

        activeElement = target
        contentContainer += activeElement

        show()
    }

    /**
     * Pause the layout:
     *
     * * pause automatic refresh processes (if necessary)
     * * remove the element shown by the layout
     * * call [hide] to hide the HTML element
     */
    override fun onPause() {
        hide()

        contentContainer -= activeElement
        activeElement = null

        super.onPause()
    }

}