/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

/**
 * Represents one inner block in the rendering of a Rui function.
 */
open class RuiFragment {

    open var ruiCreate : () -> Unit = {  }
    open var ruiPatch : () -> Unit = {  }
    open var ruiDispose : () -> Unit = {  }

    fun set(create : () -> Unit, patch : () -> Unit, dispose : () -> Unit) : RuiFragment {
        this.ruiCreate = create
        this.ruiPatch = patch
        this.ruiDispose = dispose
        return this
    }
}