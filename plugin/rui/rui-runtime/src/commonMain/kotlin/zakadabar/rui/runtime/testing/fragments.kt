/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.Rui
import zakadabar.rui.runtime.RuiAdapter
import zakadabar.rui.runtime.RuiBridge
import zakadabar.rui.runtime.RuiFragment

open class RuiTracingFragment<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiParent: RuiFragment<BT>?,
    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit
) : RuiFragment<BT> {

    val traceName = this::class.simpleName.toString()

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        ruiAdapter.trace(traceName, "mount", "bridge:", bridge)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch")
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        ruiAdapter.trace(traceName, "unmount", "bridge:", bridge)
    }

    override fun ruiDispose() {
        ruiAdapter.trace(traceName, "dispose")
    }

}

@Rui
fun T0() {
}

@Suppress("unused")
class RuiT0<BT>(
    ruiAdapter: RuiAdapter<BT>,
    ruiParent: RuiFragment<BT>?,
    ruiExternalPatch: (it: RuiFragment<BT>) -> Unit
) : RuiTracingFragment<BT>(
    ruiAdapter,
    ruiParent,
    ruiExternalPatch
)

@Rui
@Suppress("unused", "FunctionName")
fun T1(p0: Int) {
}

@Suppress("unused")
class RuiT1<BT>(
    ruiAdapter: RuiAdapter<BT>,
    ruiParent: RuiFragment<BT>?,
    ruiExternalPatch: (it: RuiFragment<BT>) -> Unit,
    var p0: Int
) : RuiTracingFragment<BT>(
    ruiAdapter,
    ruiParent,
    ruiExternalPatch
) {

    var ruiDirty0 = 0

    @Suppress("unused")
    fun ruiInvalidate0(mask: Int) {
        ruiAdapter.trace(traceName, "invalidate", "mask:", mask, "ruiDirty0", ruiDirty0)
        ruiDirty0 = ruiDirty0 or mask
    }

    init {
        ruiAdapter.trace(traceName, "init")
    }

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create", "p0:", p0)
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirty0, "p0:", p0)
        ruiDirty0 = 0
    }
}

@Rui
fun H1(@Rui builder: () -> Unit) {
    builder()
}

@Suppress("unused")
class RuiH1(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiParent: RuiFragment<TestNode>,
    ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit,
    @Rui builder: (ruiAdapter: RuiAdapter<TestNode>) -> RuiFragment<TestNode>
) : RuiC1(ruiAdapter, ruiParent, ruiExternalPatch) {

    override val fragment0 = builder(ruiAdapter)

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        super.ruiMount(bridge)
        fragment0.ruiMount(bridge)
    }

    init {
        ruiAdapter.trace(traceName, "init")
    }
}

@Suppress("unused")
abstract class RuiC1(
    ruiAdapter: RuiAdapter<TestNode>,
    parent: RuiFragment<TestNode>,
    ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit
) : RuiTracingFragment<TestNode>(ruiAdapter, parent, ruiExternalPatch) {

    abstract val fragment0: RuiFragment<TestNode>

    override fun ruiCreate() {
        super.ruiCreate()
        fragment0.ruiCreate()
    }

    override fun ruiMount(bridge: RuiBridge<TestNode>) {
        super.ruiMount(bridge)
        fragment0.ruiMount(bridge)
    }

    override fun ruiPatch() {
        super.ruiPatch()
        fragment0.ruiPatch()
    }

    override fun ruiUnmount(bridge: RuiBridge<TestNode>) {
        fragment0.ruiUnmount(bridge)
        super.ruiUnmount(bridge)
    }

    override fun ruiDispose() {
        fragment0.ruiDispose()
        super.ruiDispose()
    }
}

@Rui
@Suppress("unused", "FunctionName")
fun EH1A(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class RuiEH1A(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiParent: RuiFragment<TestNode>,
    ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit,
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : RuiTracingFragment<TestNode>(
    ruiAdapter,
    ruiParent,
    ruiExternalPatch
) {

    init {
        ruiAdapter.trace(traceName, "init", "p0:", p0)
        if (ruiAdapter is RuiTestAdapter) {
            ruiAdapter.fragments += this
        }
    }

    var ruiDirty0 = 0

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun ruiInvalidate0(mask: Int) {
        ruiAdapter.trace(traceName, "invalidate", "mask:", mask, "ruiDirty0:", ruiDirty0)
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirty0, "p0:", p0)
        ruiDirty0 = 0
    }

}

@Rui
@Suppress("unused", "FunctionName")
fun EH1B(p0: Int, eventHandler: (np0: Int) -> Unit) {
}

@Suppress("unused")
class RuiEH1B(
    ruiAdapter: RuiAdapter<TestNode>,
    ruiParent: RuiFragment<TestNode>?,
    ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit,
    var p0: Int,
    var eventHandler: (np0: Int) -> Unit,
) : RuiTracingFragment<TestNode>(
    ruiAdapter,
    ruiParent,
    ruiExternalPatch
) {

    init {
        ruiAdapter.trace(traceName, "init", "p0:", p0)
        if (ruiAdapter is RuiTestAdapter) {
            ruiAdapter.fragments += this
        }
    }

    var ruiDirty0 = 0

    override fun ruiCreate() {
        ruiAdapter.trace(traceName, "create")
    }

    @Suppress("unused")
    fun ruiInvalidate0(mask: Int) {
        ruiAdapter.trace(traceName, "invalidate", "mask:", mask, "ruiDirty0:", ruiDirty0)
        ruiDirty0 = ruiDirty0 or mask
    }

    override fun ruiPatch() {
        ruiAdapter.trace(traceName, "patch", "ruiDirty0:", ruiDirty0, "p0:", p0)
        ruiDirty0 = 0
    }

}