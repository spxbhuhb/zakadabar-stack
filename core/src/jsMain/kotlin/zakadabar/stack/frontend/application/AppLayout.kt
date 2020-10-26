/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import zakadabar.stack.frontend.application.navigation.NavState
import zakadabar.stack.frontend.application.navigation.NavTarget
import zakadabar.stack.frontend.elements.ComplexElement

abstract class AppLayout(val name: String) : ComplexElement() {

    val navTargets = mutableListOf<NavTarget>()

    abstract fun resume(state: NavState)

    abstract fun pause()

}