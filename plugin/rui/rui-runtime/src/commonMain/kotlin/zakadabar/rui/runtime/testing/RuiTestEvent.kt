/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiFragment

class RuiTestEvent(
    val type: RuiTestEvents,
    val fragment: RuiFragment<*>,
    vararg val arguments : Any
) {

    fun dump() : String {
        val name = fragment::class.simpleName ?: ""
        return "${type.name.padEnd(15, ' ')}${name.padEnd(20, ' ')} ${arguments.joinToString(" ")}"
    }

}