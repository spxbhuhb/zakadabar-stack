/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBridge
import zakadabar.rui.runtime.RuiFragment

open class RuiTestAdapter : RuiAdapter<TestNode> {

    val events = mutableListOf<RuiTestEvent>()

    val fragments = mutableListOf<RuiFragment<TestNode>>()

    override fun createPlaceholder(): RuiBridge<TestNode> {
        return RuiTestBridge()
    }

    open fun clear() {
        events.clear()
    }

    fun printDump() {
        println(dump())
    }

    fun dump() : String =
        events.joinToString("\n") { it.dump() }

}