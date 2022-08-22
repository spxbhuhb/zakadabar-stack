/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.run

import zakadabar.rui.kotlin.plugin.RuiTest
import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.EH1
import zakadabar.rui.runtime.testing.RuiEH1
import zakadabar.rui.runtime.testing.RuiTestAdapter

@Suppress("unused")
@RuiTest
fun eventHandlerTest() {
    val adapter = RuiTestAdapter()

    rui(adapter) {
        eventHandlerFragment()
    }

    val rootNode = adapter.rootBridge.receiver

    val eh1 = adapter.fragments.first() as RuiEH1
    eh1.eventHandler(12)

    // TODO check test results with asserts
}

@Rui
fun eventHandlerFragment() {
    var i = 12
    EH1(i + 1) { i++ }
}