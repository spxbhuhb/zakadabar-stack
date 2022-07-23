/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFunWrapper
import zakadabar.rui.runtime.RuiWhen
import zakadabar.rui.runtime.ruiEmptyBlockFunc

fun Branch(value: Int) {
    val v2 = 12
    // ---- boundary ----
    Primitive(v2)
    when (value) {
        1 -> Primitive(value + 10)
        2 -> Primitive(value + 20)
    }
}


class RuiBranchPoc(
    // state variables from function parameters
    var value: Int
) : RuiFragment() {

    // state variables from top-level declarations
    var v2 = 12

    var dirty0 = 0

    fun invalidate0(mask : Int) {
        dirty0 = dirty0 and mask
    }

    init {
        // top-level blocks from rendering

        val primitive0 = RuiPrimitiveFragment(v2)

        val branch0 = RuiWhen {
            when (value) {
                1 -> block0func
                2 -> block1func
                else -> ruiEmptyBlockFunc
            }
        }

        // lifecycle

        fun create() {
            primitive0.ruiCreate()
            branch0.ruiCreate()
        }

        fun patch() {
            if (dirty0 and 1 != 0) {
                branch0.ruiPatch()
            }
            dirty0 = 0
        }

        fun dispose() {
            primitive0.ruiDispose()
            branch0.ruiDispose()
        }

        set(::create, ::patch, ::dispose)
    }

    val block0func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveFragment(value + 10)

        fun create() {
            primitive0.ruiCreate()
        }

        fun patch() {
            primitive0.value = value + 10
            primitive0.invalidate0(1)
            primitive0.ruiPatch()
        }

        fun dispose() {
            primitive0.ruiDispose()
        }

        RuiFragment().set(::create, ::patch, ::dispose)
    }

    val block1func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveFragment(value + 20)

        fun create() {
            primitive0.ruiCreate()
        }

        fun patch() {
            primitive0.value = value + 20
            primitive0.dirty0 = 1
            primitive0.ruiPatch()
        }

        fun dispose() {
            primitive0.ruiDispose()
        }

        RuiFragment().set(::create, ::patch, ::dispose)
    }

}