/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.core

/**
 * Base class for all RUI components.
 */
open class RuiComponentBase {

    open var dirty = arrayOf(0)

    open fun create() {}
    open fun patch() {}
    open fun dispose() {}

    /**
     * Invalidates one state variable by its index.
     */
    open fun invalidate(index : Int) {
        val arrayIndex = index / 32
        val bitIndex = index % 32
        dirty[arrayIndex] = dirty[arrayIndex] or bitIndex
    }
}