/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.run

import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance
import zakadabar.rui.kotlin.plugin.RuiTest
import zakadabar.rui.kotlin.plugin.RuiTestDumpResult
import zakadabar.rui.kotlin.plugin.RuiTestResult
import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.EH1
import zakadabar.rui.runtime.testing.RuiEH1
import zakadabar.rui.runtime.testing.RuiTestAdapter

@RuiTest
@RuiTestDumpResult
fun eventHandlerTest() {
    val adapter = RuiTestAdapter()

    rui(adapter) {
        eventHandlerFragment()
    }

    adapter.fragments.firstIsInstance<RuiEH1>().eventHandler(12)
}

@Rui
fun eventHandlerFragment() {
    var i = 12
    EH1(i + 1) { i++ }
}

@RuiTestResult
fun eventHandlerTestResult() : String = """
[ RuiRoot                        ]  init                  |  
[ RuiEventHandlerFragment        ]  init                  |  
[ RuiEH1                         ]  init                  |  p0: 13
[ RuiRoot                        ]  create                |  
[ RuiEventHandlerFragment        ]  create                |  
[ RuiEH1                         ]  create                |  
[ RuiRoot                        ]  mount                 |  
[ RuiEventHandlerFragment        ]  mount                 |  
[ RuiEH1                         ]  mount                 |  bridge: 1
[ RuiEventHandlerFragment        ]  patch                 |  ruiDirty0: 1
[ RuiEH1                         ]  invalidate            |  mask: 1 ruiDirty0: 0
[ RuiEH1                         ]  patch                 |  ruiDirty0: 1 p0: 14
""".trimIndent()