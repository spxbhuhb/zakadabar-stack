/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
class Desktop(
    val header: ZkElement? = DesktopHeader(),
    val center: ZkElement? = DesktopCenter(),
    val footer: ZkElement? = DesktopFooter()
) : ZkElement() {

    // ----------------------------------------------------------------
    // Lifecycle
    // ----------------------------------------------------------------

    override fun init(): ZkElement {
        super.init()

        element.className = desktopClasses.desktop

        this += header
        this += center
        this += footer

        return this
    }

}