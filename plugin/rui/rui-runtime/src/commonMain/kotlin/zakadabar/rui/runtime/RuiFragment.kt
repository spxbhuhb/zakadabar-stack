/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

/**
 * Represents one inner block in the rendering of a Rui function.
 */
open class RuiFragment(
    val ruiAdapter : RuiAdapter
) {

    open fun ruiCreate() {  }
    open fun ruiMount(anchor : RuiAnchor) {  }
    open fun ruiPatch() {  }
    open fun ruiUnmount() {  }
    open fun ruiDispose() {  }

}