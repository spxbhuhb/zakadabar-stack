/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.core

/**
 * Base class for all RUI components.
 */
open class RuiComponentBase {

    open var dirty = arrayOf(0)

    open fun patch(mask: Array<Int>) {}
    open fun dispose() {}

}