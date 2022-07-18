/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.core.RuiBlock
import zakadabar.rui.core.RuiBranch
import zakadabar.rui.core.RuiFunWrapper
import zakadabar.rui.core.ruiEmptyBlockFunc
import zakadabar.rui.kotlin.plugin.RuiPrimitiveBlock

class RuiBranchPocTest(
    // state variables from function parameters
    var value: Int
) : RuiBlock() {

    // state variables from top-level declarations
    var v2 = 12

    init {
        // top-level blocks from rendering

        val primitive0 = RuiPrimitiveBlock(v2)

        val branch0 = RuiBranch {
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