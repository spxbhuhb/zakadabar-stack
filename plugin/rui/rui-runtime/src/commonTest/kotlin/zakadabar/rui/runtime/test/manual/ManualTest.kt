/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import kotlin.test.Test

class ManualTest {

    @Test
    fun branchTest() {
        val c = TestBranch("<root>")
        c.ruiCreate()

        fun v(value : Int) {
            c.v0 = value
            c.ruiInvalidate0(1)
            c.ruiPatch()
        }

        v(1)
        v(2)
        v(3)
        v(1)
    }
}