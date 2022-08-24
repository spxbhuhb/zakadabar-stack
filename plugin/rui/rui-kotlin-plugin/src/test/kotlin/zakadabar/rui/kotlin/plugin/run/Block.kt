/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.run

import zakadabar.rui.kotlin.plugin.RuiTest
import zakadabar.rui.kotlin.plugin.RuiTestResult
import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.rui
import zakadabar.rui.runtime.testing.T1

@RuiTest
fun blockTest() {
    rui {
        block(10)
    }
}

@Rui
fun block(i : Int) {
    T1(i + 1)
    T1(i + 2)
}

@RuiTestResult
fun blockTestResult() : String = """
[ RuiRoot                        ]  init                  |  
[ RuiBlock                       ]  init                  |  i: 10
[ RuiT1                          ]  init                  |  
[ RuiT1                          ]  init                  |  
[ RuiRoot                        ]  create                |  
[ RuiBlock                       ]  create                |  
[ RuiT1                          ]  create                |  p0: 11
[ RuiT1                          ]  create                |  p0: 12
[ RuiRoot                        ]  mount                 |  
[ RuiBlock                       ]  mount                 |  
[ RuiT1                          ]  mount                 |  bridge: 1
[ RuiT1                          ]  mount                 |  bridge: 1
""".trimIndent()