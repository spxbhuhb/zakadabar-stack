/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses

/**
 * Contains [DockedElement]s and shows them over the normal content. The items
 * may be minimized, normal sized or maximized. Useful to pick out elements
 * from the normal document flow, mostly for editing. Check the cookbook for
 * examples.
 */
class Dock : ComplexElement() {

    override fun init(): Dock {
        className = coreClasses.dock
        return this
    }
}