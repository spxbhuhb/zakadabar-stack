/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.RuiFragmentFactory
import zakadabar.rui.runtime.RuiPlaceholder
import zakadabar.rui.runtime.RuiWhen
import zakadabar.rui.runtime.testing.RuiC1
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.RuiTestAdapter
import zakadabar.rui.runtime.testing.TestNode

//@Suppress("UNUSED_PARAMETER")
//fun rui(@RuiRoot block : (ruiAdapter : RuiAdapter) -> Unit) {
//    block(RuiAdapterRegistry.adapterFor())
//}

//@Rui
//fun Basic(i : Int) {
//    var i2 = 1
//
//    fun na() {
//
//    }
//
//    fun ca() {
//        i2++
//    }
//
//    T1(i2)
//    T0()
//}

//fun a(b : Int) {
//    if (b == 8) Unit
//
//    if (b == 9) Unit else Unit
//
//    when {
//        b == 1 -> Unit
//        b == 2 -> Unit
//    }
//
//    when {
//        b == 3 -> Unit
//        b == 4 -> Unit
//        else -> Unit
//    }
//
//    when (b) {
//        5 -> Unit
//    }
//
//}
class Branch : RuiC1(RuiTestAdapter(), { }) {

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiBranch3: RuiFragmentFactory<TestNode, RuiT1<TestNode>>
    val ruiBranch4: RuiFragmentFactory<TestNode, RuiT1<TestNode>>
    val ruiBranch5: RuiFragmentFactory<TestNode, RuiPlaceholder<TestNode>>

    override val fragment0: RuiFragment<TestNode>

    init {
        ruiBranch3 = RuiFragmentFactory {
            RuiT1(
                ruiAdapter,
                {
                    it as RuiT1
                    if (ruiDirty0 and 1 != 0) {
                        it.p0 = v0 + 10
                        ruiInvalidate0(1)
                    }
                },
                v0 + 10
            )
        }

        ruiBranch4 = RuiFragmentFactory {
            RuiT1(
                ruiAdapter,
                {
                    it as RuiT1
                    if (ruiDirty0 and 1 != 0) {
                        it.p0 = v0 + 20
                        ruiInvalidate0(1)
                    }
                },
                v0 + 20
            )
        }

        ruiBranch5 = RuiFragmentFactory { RuiPlaceholder(ruiAdapter) }

        fragment0 = RuiWhen(
            ruiAdapter,
            {
                when {
                    v0 == 1 -> 0
                    v0 == 2 -> 1
                    else -> 2
                }
            },
            ruiBranch3,
            ruiBranch4,
            ruiBranch5
        )
    }
}