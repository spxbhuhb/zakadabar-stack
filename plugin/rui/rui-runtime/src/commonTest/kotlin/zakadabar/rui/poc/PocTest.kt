/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import kotlin.test.Test

class PocTest {

    @Test
    fun higherOrderTest() {
        val c = RuiHigherOrderCallBase(1) // should print 2
        c.value = 2
        c.patch(arrayOf(1)) // should print 3
    }

    @Test
    fun branchTest() {
        val c = RuiBranchPoc(1)
        c.create()

        fun v(value : Int) {
            c.value = value
            c.dirty = 1
            c.patch()
        }

        v(1)
        v(2)
        v(3)
        v(1)
    }
}