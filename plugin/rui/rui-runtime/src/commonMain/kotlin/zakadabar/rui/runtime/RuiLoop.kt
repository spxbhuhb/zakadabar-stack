/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

abstract class RuiLoop<T>(
    ruiAdapter: RuiAdapter,
) : RuiFragment(ruiAdapter) {

    var loopVariable : T? = null

    val fragments = mutableListOf<RuiFragment>()

    abstract fun makeFragment() :RuiFragment

    abstract fun makeIterator() : Iterator<T>

    override fun ruiCreate() {
        for (loopVariable in makeIterator()) {
            this.loopVariable = loopVariable
            fragments.add(makeFragment())
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
                // FIXME f.ruiMount()
            } else {
                fragments[index].ruiPatch()
            }
            index ++
        }
        while (index < fragments.size) {
            val f = fragments.removeLast()
            f.ruiUnmount()
            f.ruiDispose()
            index ++
        }
    }

    override fun ruiDispose() {
        for (f in fragments) {
            f.ruiDispose()
        }
    }
}