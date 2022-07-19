/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiBlock
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
) : RuiBlock() {

    // state variables from top-level declarations
    var v2 = 12

    init {
        // top-level blocks from rendering

        val primitive0 = RuiPrimitiveBlock(v2)

        val branch0 = RuiWhen {
            when (value) {
                1 -> block0func
                2 -> block1func
                else -> ruiEmptyBlockFunc
            }
        }

        // lifecycle

        fun create() {
            primitive0.create()
            branch0.create()
        }

        fun patch() {
            if (dirty and 1 != 0) {
                branch0.patch()
            }
            dirty = 0
        }

        fun dispose() {
            primitive0.dispose()
            branch0.dispose()
        }

        set(::create, ::patch, ::dispose)
    }

    val block0func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveBlock(value + 10)

        fun create() {
            primitive0.create()
        }

        fun patch() {
            primitive0.value = value + 10
            primitive0.dirty = 1
            primitive0.patch()
        }

        fun dispose() {
            primitive0.dispose()
        }

        RuiBlock().set(::create, ::patch, ::dispose)
    }

    val block1func = RuiFunWrapper {
        val primitive0 = RuiPrimitiveBlock(value + 20)

        fun create() {
            primitive0.create()
        }

        fun patch() {
            primitive0.value = value + 20
            primitive0.dirty = 1
            primitive0.patch()
        }

        fun dispose() {
            primitive0.dispose()
        }

        RuiBlock().set(::create, ::patch, ::dispose)
    }

}