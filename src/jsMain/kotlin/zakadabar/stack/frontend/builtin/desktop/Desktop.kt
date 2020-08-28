/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.util.UUID

class Desktop : ComplexElement() {

    companion object : ViewContract() {

        override val uuid = UUID("a53a5358-b942-4092-ad53-697ef64b7521")

        val header = UUID("164fa326-886b-4102-b8c7-6e3a23f51147")

        val center = UUID("acb06b7e-88f2-4e1c-a5b7-e69899577c2e")

        val footer = UUID("a248d39e-7d91-4573-bc5b-2cd1994e49bf")

        override fun newInstance() = Desktop()

    }

    private val headerInstance = FrontendContext.newInstance(header, ComplexElement::class)
    private var centerInstance = FrontendContext.newInstance(center, ComplexElement::class)
    private var footerInstance = FrontendContext.newInstance(footer, ComplexElement::class)

    // ----------------------------------------------------------------
    // Lifecycle
    // ----------------------------------------------------------------

    override fun init(): ComplexElement {
        super.init()

        element.className = desktopClasses.desktop

        this += headerInstance
        this += centerInstance
        this += footerInstance

        return this
    }

}