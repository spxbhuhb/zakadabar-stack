/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.test.manual

import zakadabar.rui.runtime.*
import zakadabar.rui.runtime.testing.RuiT1
import zakadabar.rui.runtime.testing.TestNode


@Suppress("JoinDeclarationAndAssignment", "unused")
class Branch(
    override val ruiAdapter: RuiAdapter<TestNode>
) : RuiGeneratedFragment<TestNode> {

    override val ruiParent: RuiFragment<TestNode>? = null
    override val ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit = {  }

    override val fragment: RuiFragment<TestNode>

    var v0: Int = 1

    var ruiDirty0 = 0

    fun ruiInvalidate0(mask: Int) {
        ruiDirty0 = ruiDirty0 or mask
    }

    fun ruiEp0(it: RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1 != 0) {
            it.p0 = v0 + 10
            ruiInvalidate0(1)
        }
    }

    fun ruiEp1(it: RuiFragment<TestNode>) {
        it as RuiT1
        if (ruiDirty0 and 1 != 0) {
            it.p0 = v0 + 20
            ruiInvalidate0(1)
        }
    }

    fun ruiBranch0(): RuiFragment<TestNode> = RuiT1(ruiAdapter, this, ::ruiEp0, v0 + 10)
    fun ruiBranch1(): RuiFragment<TestNode> = RuiT1(ruiAdapter, this, ::ruiEp1, v0 + 20)
    fun ruiBranch2(): RuiFragment<TestNode> = RuiPlaceholder(ruiAdapter)

    fun ruiSelect(): Int =
        when (v0) {
            1 -> 0 // index in RuiWhen.fragments
            2 -> 1
            else -> 2
        }

    init {
        fragment = RuiWhen(
            ruiAdapter,
            ::ruiSelect,
            ::ruiBranch0,
            ::ruiBranch1,
            ::ruiBranch2
        )
    }
}