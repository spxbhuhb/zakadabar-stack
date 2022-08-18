/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

open class RuiWhen<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    val ruiSelect: () -> RuiFragmentFactory<BT, out RuiFragment<BT>>
) : RuiFragment<BT> {

    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit = { }

    lateinit var placeholder: RuiBridge<BT>

    var branch: RuiFragmentFactory<BT, out RuiFragment<BT>> = ruiSelect()
    var fragment: RuiFragment<BT> = branch.builder()

    override fun ruiCreate() {
        fragment.ruiCreate()
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        placeholder = ruiAdapter.createPlaceholder()
        bridge.add(placeholder)

        fragment.ruiMount(placeholder)
    }

    override fun ruiPatch() {
        val newBranch = ruiSelect()
        if (newBranch == branch) {
            fragment.ruiPatch()
        } else {
            fragment.ruiUnmount(placeholder)
            fragment.ruiDispose()
            fragment = branch.builder()
            fragment.ruiCreate()
            fragment.ruiMount(placeholder)
        }
    }

    override fun ruiUnmount(bridge: RuiBridge<BT>) {
        fragment.ruiUnmount(placeholder)
        bridge.remove(placeholder)
    }

    override fun ruiDispose() {
        fragment.ruiDispose()
    }

}