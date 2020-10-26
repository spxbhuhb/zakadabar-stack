/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.stack.frontend.elements.ComplexElement

class Tortuga : ComplexElement() {

    override fun init(): ComplexElement {
        this build {
            + "Tortuga"
        }

        return this
    }

}