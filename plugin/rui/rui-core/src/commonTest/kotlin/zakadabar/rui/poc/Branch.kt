/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.core.RuiComponentBase

fun Branch(value: Int) {
    when (value) {
        1 -> Primitive(value + 1)
        2 -> Primitive(value + 2)
    }
}

class RuiBranchBase(
    var value: Int
) : RuiComponentBase() {

    var branch0index = - 1
    var branch0: RuiComponentBase? = null

    init {
        branch0patch()
    }

    fun branch0patch() {
        when (value) {
            1 -> if (branch0index == 0) {
                branch0?.patch(dirty)
            } else {
                branch0?.dispose()
                branch0 = RuiBlock0Base()
                branch0index = 0
            }
            2 -> if (branch0index == 1) {
                branch0?.patch(dirty)
            } else {
                branch0?.dispose()
                branch0 = RuiBlock1Base()
                branch0index = 1
            }
            else -> if (branch0index != - 1) {
                branch0?.dispose()
                branch0 = null
                branch0index = -1
            }
        }
    }

    inner class RuiBlock0Base : RuiComponentBase() {
        val primitive0 = RuiPrimitiveBase(value)
        override fun patch(mask: Array<Int>) {
            dirty = mask
            val d0 = this@RuiBranchBase.dirty[0]
            if (d0 and 1 != 0) {
                primitive0.value = value + 1
                primitive0.patch(arrayOf(1))
            }
        }
    }

    inner class RuiBlock1Base : RuiComponentBase() {
        val primitive0 = RuiPrimitiveBase(value)
        override fun patch(mask: Array<Int>) {
            dirty = mask
            val d0 = this@RuiBranchBase.dirty[0]
            if (d0 and 1 != 0) {
                primitive0.value = value + 2
                primitive0.patch(arrayOf(1))
            }
        }
    }

    override fun patch(mask: Array<Int>) {
        dirty = mask
        if (mask[0] and 1 != 0) {
            branch0patch()
        }
    }

}