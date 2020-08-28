/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.util.UUID

abstract class ViewContract {

    open val uuid = UUID.NIL

    open val target = UUID.NIL

    var installOrder = 0
    open var priority = 0
    open var position = 0

    abstract fun newInstance(): Any

    fun newElement() = newInstance() as ComplexElement

    override fun toString() =
        "${this::class.simpleName}(self=$uuid, target=$target, priority=$priority, position=$position, installOrder=$installOrder)"

}