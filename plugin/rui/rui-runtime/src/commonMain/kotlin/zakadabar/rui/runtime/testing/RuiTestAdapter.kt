/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFragmentFactory

object RuiTestAdapter : RuiAdapter {

    val events = mutableListOf<RuiTestEvent>()

    override fun insert(fragment: RuiFragment) {
        events += RuiTestEvent(RuiTestEvents.Insert, fragment)
    }

    override fun remove(fragment: RuiFragment) {
        events += RuiTestEvent(RuiTestEvents.Remove, fragment)
    }

    override val emptyFragmentFactory = RuiFragmentFactory { adapter: RuiAdapter, anchor: RuiFragment? ->
        RuiTestEmptyFragment(adapter)
    }

    fun clear() {
        events.clear()
    }

    fun printDump() {
        println(dump())
    }

    fun dump() : String =
        events.joinToString("\n") { it.dump() }

}