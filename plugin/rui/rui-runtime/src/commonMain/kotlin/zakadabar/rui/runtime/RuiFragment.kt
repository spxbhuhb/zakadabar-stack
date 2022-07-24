/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

/**
 * Represents one inner block in the rendering of a Rui function.
 */
open class RuiFragment(
    val ruiAdapter : RuiAdapter,
    val ruiAnchor : RuiFragment?,
    val ruiPatchState : (it : RuiFragment) -> Unit
) {

    open fun ruiCreate() {  }
    open fun ruiPatchRender() {  }
    open fun ruiDispose() {  }

}