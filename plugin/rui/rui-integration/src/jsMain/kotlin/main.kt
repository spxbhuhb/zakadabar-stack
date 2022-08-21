/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.rui.runtime.dom.RuiDOMAdapter
import zakadabar.rui.runtime.dom.html.Button
import zakadabar.rui.runtime.dom.html.Text
import zakadabar.rui.runtime.rui

fun main() {
    rui(RuiDOMAdapter()) {
        var counter = 0
        Button("Counter: $counter") { counter++ }
        Text("You've clicked $counter times.")
    }
}