/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.core

/**
 * Represents one inner block in the rendering of a Rui function.
 */
open class RuiBlock {
    open var dirty : Int = 0

    open var create : () -> Unit = {  }
    open var patch : () -> Unit = {  }
    open var dispose : () -> Unit = {  }

    fun set(create : () -> Unit, patch : () -> Unit, dispose : () -> Unit) : RuiBlock {
        this.create = create
        this.patch = patch
        this.dispose = dispose
        return this
    }
}