/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.navigator

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.FrontendEntitySupport

class NewEntityItem(
    private val newEntity: NewEntity,
    internal val support: FrontendEntitySupport<*>
) : ComplexElement() {

    override fun init(): ComplexElement {
        super.init()

        className = navigatorClasses.newEntityItem

        this += (support.iconSource?.simple16 ?: Icons.description.simple16).withClass(navigatorClasses.newEntityIcon)
        this += SimpleText(support.displayName).withClass(navigatorClasses.newEntityName)

        on("click", ::onClick)

        return this
    }

    private fun onClick(@Suppress("UNUSED_PARAMETER") event: Event) {
        newEntity.selected = support
        newEntity.next()
    }
}
