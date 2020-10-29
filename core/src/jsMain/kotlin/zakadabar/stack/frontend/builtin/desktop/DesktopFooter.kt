/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

@PublicApi
class DesktopFooter : ZkElement() {

    override fun init(): DesktopFooter {
        super.init()

        className = desktopClasses.footer
        innerHTML += """<div class="${desktopClasses.copyright}">${t("copyright")}</div>"""

        return this
    }

}