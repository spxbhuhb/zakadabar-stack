/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.kotlin.plugin.RuiPrimitiveFragment
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFunWrapper
import zakadabar.rui.runtime.RuiWhen
import zakadabar.rui.runtime.ruiEmptyBlockFunc

class RuiBranchPocTest(
    // state variables from function parameters
    var value: Int
) : RuiFragment() {

    // state variables from top-level declarations
    var v2 = 12

    var dirty0 = 0

    // CALL BLOCK --------------------------------------------

    val block0 = RuiPrimitiveFragment(value + 10)

    fun block0patch() {
        if (dirty0 and 1 != 0) {
            block0.value = value + 10
            block0.invalidate0(1)
        }
        if (block0.dirty0 != 0) {
            block0.patch()
        }
    }

    // BRANCH BLOCK --------------------------------------------

    val block1 = RuiWhen {
        when (value) {
            1 -> block2func
            2 -> block3func
            else -> ruiEmptyBlockFunc
        }
    }

    val block2func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveFragment(value + 10)

        fun create() {
            primitive0.create()
        }

        fun patch() {
            primitive0.value = value + 10
            primitive0.invalidate0(1)
            primitive0.patch()
        }

        fun dispose() {
            primitive0.dispose()
        }

        RuiFragment().set(::create, ::patch, ::dispose)
    }

    val block3func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveFragment(value + 20)

        fun create() {
            primitive0.create()
        }

        fun patch() {
            primitive0.value = value + 20
            primitive0.dirty0 = 1
            primitive0.patch()
        }

        fun dispose() {
            primitive0.dispose()
        }

        RuiFragment().set(::create, ::patch, ::dispose)
    }

    // Component lifecycle

    fun create0() {
        block0.create()
        block1.create()
    }

    fun patch0() {
        if (dirty0 and 1 != 0) {
            block0patch()
        }
        if (dirty0 and 1 != 0) {
            block1.patch()
        }
        dirty0 = 0
    }

    fun dispose0() {
        block0.dispose()
        block1.dispose()
    }

    init {
        set(::create0, ::patch0, ::dispose0)
    }
}