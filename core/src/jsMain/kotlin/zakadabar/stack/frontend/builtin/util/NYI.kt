/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.elements.ComplexElement

class NYI(val message: String = t("not yet implemented")) : ComplexElement() {

    override fun init(): ComplexElement {
        this.innerText = message
        return super.init()
    }

}