/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.ReactiveComponent

fun HigherOrderCall(value : Int) {
    HigherOrderFun {
        Primitive(value + 1)
    }
}

class ReactiveHigherOrderCall(
    var value : Int
): ReactiveComponent() {

    var higherOrderFun0 = ReactiveHigherOrderFun { ReactiveBlock0() }

    inner class ReactiveBlock0 : ReactiveComponent() {

        var primitive0 = ReactivePrimitive(value + 1)

        override fun patch(mask : Array<Int>) {
            dirty = mask
            val d0 = this@ReactiveHigherOrderCall.dirty[0]
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