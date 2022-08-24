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
import zakadabar.rui.runtime.testing.EH1A
import zakadabar.rui.runtime.testing.EH1B
import zakadabar.rui.runtime.testing.RuiEH1A
import zakadabar.rui.runtime.testing.RuiTestAdapter

@RuiTest
@RuiTestDumpResult
fun ifElse() {
    val adapter = RuiTestAdapter()

    rui(adapter) {
        ifElseFragment()
    }

    adapter.fragments.firstIsInstance<RuiEH1A>().eventHandler(13)
    adapter.fragments.firstIsInstance<RuiEH1A>().eventHandler(14)
}

@Rui
fun ifElseFragment() {
    var i = 12
    if (i % 2 == 0) {
        EH1A(i + 10) { i ++ }
    } else {
        EH1B(i + 20) { i ++ }
    }
}

@RuiTestResult
fun ifElseResult() : String = """
[ RuiRoot                        ]  init                  |  
[ RuiIfElseFragment              ]  init                  |  
[ RuiEH1A                        ]  init                  |  p0: 22
[ RuiRoot                        ]  create                |  
[ RuiIfElseFragment              ]  create                |  
[ RuiEH1A                        ]  create                |  
[ RuiRoot                        ]  mount                 |  
[ RuiIfElseFragment              ]  mount                 |  
[ RuiEH1A                        ]  mount                 |  bridge: 2
[ RuiIfElseFragment              ]  patch                 |  ruiDirty0: 1
[ RuiEH1A                        ]  unmount               |  bridge: 2
[ RuiEH1A                        ]  dispose               |  
[ RuiEH1B                        ]  init                  |  p0: 33
[ RuiEH1B                        ]  create                |  
[ RuiEH1B                        ]  mount                 |  bridge: 2
[ RuiIfElseFragment              ]  patch                 |  ruiDirty0: 1
[ RuiEH1B                        ]  unmount               |  bridge: 2
[ RuiEH1B                        ]  dispose               |  
[ RuiEH1A                        ]  init                  |  p0: 24
[ RuiEH1A                        ]  create                |  
[ RuiEH1A                        ]  mount                 |  bridge: 2
""".trimIndent()