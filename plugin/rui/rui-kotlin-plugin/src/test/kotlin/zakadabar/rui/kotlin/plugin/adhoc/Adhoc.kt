/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBlock
import zakadabar.rui.runtime.RuiBridge
import zakadabar.rui.runtime.RuiFragment
import zakadabar.rui.runtime.testing.RuiT0
import zakadabar.rui.runtime.testing.RuiT1

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

class Block<BT>(
    override val ruiAdapter : RuiAdapter<BT>,
    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit
) : RuiFragment<BT> {

    var v0 = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    val ruiB0T10: RuiT1<BT>
    val ruiB0T01: RuiT0<BT>
    val fragment0: RuiBlock<BT>

    init {
        ruiB0T10 = RuiT1(ruiAdapter, {
            it as RuiT1
            if (ruiDirty0 and 1 != 0) {
                it.p0 = v0
                it.ruiInvalidate0(1)
                it.ruiPatch()
            }
        }, v0)

        ruiB0T01 = RuiT0(ruiAdapter, {})
        fragment0 = RuiBlock(
            ruiAdapter,
            ruiB0T10,
            ruiB0T01
        )
    }

    override fun ruiCreate() {
        TODO("Not yet implemented")
    }

    override fun ruiPatch() {
        TODO("Not yet implemented")
    }

    override fun ruiDispose() {
        TODO("Not yet implemented")
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        TODO("Not yet implemented")
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        TODO("Not yet implemented")
    }

}