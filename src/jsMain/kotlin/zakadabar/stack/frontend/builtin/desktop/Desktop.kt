/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.util.PublicApi

@PublicApi
class Desktop(
    val header: ComplexElement? = DesktopHeader(),
    val center: ComplexElement? = DesktopCenter(),
    val footer: ComplexElement? = DesktopFooter()
) : ComplexElement() {

    // ----------------------------------------------------------------
    // Lifecycle
    // ----------------------------------------------------------------

    override fun init(): ComplexElement {
        super.init()

        element.className = desktopClasses.desktop

        this += header
        this += center
        this += footer

        return this
    }

}