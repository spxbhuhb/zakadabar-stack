/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.witchesbrew.frontend

import zakadabar.samples.witchesbrew.frontend.CauldronClasses.Companion.cauldronClasses
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.elements.SimpleElement

class RealSimple : SimpleElement() {

    private val css = cauldronClasses

    override fun init(): SimpleElement {

        className = css.realSimple

        build {
            + div(css.text1) { + t("text1") }
            + div(css.text2) { ! """<span style="font-size: 150%">↫</span> ${t("text2")}""" }
        }

        return super.init()
    }
}