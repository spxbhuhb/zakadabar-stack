/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

class RuiLoop<BT,IT>(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiExternalPatch: (it: RuiFragment<BT>) -> Unit,
    val makeIterator : () -> Iterator<IT>,
    val makeFragment : () -> RuiFragment<BT>
) : RuiFragment<BT> {

    var loopVariable : IT? = null

    val fragments = mutableListOf<RuiFragment<BT>>()

    lateinit var placeholder: RuiBridge<BT>

    override fun ruiCreate() {
        for (loopVariable in makeIterator()) {
            this.loopVariable = loopVariable
            fragments.add(makeFragment())
        }
    }

    override fun ruiMount(bridge: RuiBridge<BT>) {
        placeholder = ruiAdapter.createPlaceholder()
        bridge.add(placeholder)

        for (fragment in fragments) {
            fragment.ruiMount(placeholder)
        }
    }

    @Suppress("UseWithIndex")
    override fun ruiPatch() {
        var index = 0
        for (loopVariable in makeIterator()) {
            this.loopVariable = loopVariable
            if (index >= fragments.size) {
                val f = makeFragment()
                fragments.add(f)
                f.ruiCreate()
                f.ruiMount(placeholder)
            } else {
                fragments[index].ruiPatch()
            }
            index ++
        }
        while (index < fragments.size) {
            val f = fragments.removeLast()
            f.ruiUnmount(placeholder)
            f.ruiDispose()
            index ++
        }
    }

    override fun ruiUnmount(bridge : RuiBridge<BT>) {
        for (fragment in fragments) {
            fragment.ruiUnmount(placeholder)
        }
        bridge.remove(placeholder)
    }

    override fun ruiDispose() {
        for (f in fragments) {
            f.ruiDispose()
        }
    }
}