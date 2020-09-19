/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.frontend.elements.ComplexElement

interface ViewCompanion {

    fun newInstance(scope: Any? = null): ComplexElement

}