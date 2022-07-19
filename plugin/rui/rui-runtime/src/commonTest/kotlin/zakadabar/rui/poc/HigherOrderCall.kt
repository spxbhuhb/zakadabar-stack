/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.poc

import zakadabar.rui.runtime.RuiComponentBase

fun HigherOrderCall(value : Int) {
    HigherOrderFun {
        Primitive(value + 1)
    }
}

class RuiHigherOrderCallBase(
    var value : Int
): RuiComponentBase() {

    var higherOrderFun0 = RuiHigherOrderFunBase { RuiBlock0Base() }

    inner class RuiBlock0Base : RuiComponentBase() {

        var primitive0 = RuiPrimitive(value + 1)

        override fun patch(mask : Array<Int>) {
            dirty = mask
            val d0 = this@RuiHigherOrderCallBase.dirty[0]
            if (d0 and 1 != 0) {
                primitive0.value = value + 1
                primitive0.patch(arrayOf(1))
            }
        }

        override fun dispose() {
            primitive0.dispose()
        }
    }

    override fun patch(mask: Array<Int>) {
        dirty = mask
        val d0 = mask[0]
        if (d0 and 1 != 0) higherOrderFun0.patch(arrayOf(1))
    }

    override fun dispose() {
        higherOrderFun0.dispose()
    }

}