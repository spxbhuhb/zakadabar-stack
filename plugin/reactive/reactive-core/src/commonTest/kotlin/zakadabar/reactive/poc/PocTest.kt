/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import kotlin.test.Test

class PocTest {

    @Test
    fun buttonTest() {
        val component = ReactiveButton("label", "icon")
        component.c()
        component.s(1, "label2")
    }

    @Test
    fun branchTest() {
        val component = ReactiveBranch(1)
        component.c()
        component.s(1, 1)
        component.s(1, 2)
        component.s(1, 3)
        component.s(1, 1)
    }
}