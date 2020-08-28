/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID

@PublicApi
class DesktopFooter : ComplexElement() {

    companion object : ViewContract() {

        override val uuid = UUID("fc03ac6c-260c-4fec-997a-7ce26bd14c85")

        override val target = Desktop.footer

        override fun newInstance() = DesktopFooter()

    }

    override fun init(): ComplexElement {
        super.init()

        className = desktopClasses.footer
        innerHTML += """<div class="${desktopClasses.copyright}">${t("copyright")}</div>"""

        return this
    }

}