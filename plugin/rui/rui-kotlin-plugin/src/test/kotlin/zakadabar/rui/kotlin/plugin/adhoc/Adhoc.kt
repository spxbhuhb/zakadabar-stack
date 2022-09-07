/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.adhoc

class E {
    fun b1() = Unit
    val b = B(this::b1)
    val c = B(this::b1)
}

class B(
    val func: () -> Unit
)

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
//
//@Suppress("JoinDeclarationAndAssignment", "unused")
//class Branch(
//    override val ruiAdapter: RuiAdapter<TestNode>
//) : RuiGeneratedFragment<TestNode> {
//
//    override val ruiParent: RuiFragment<TestNode>? = null
//    override val ruiExternalPatch: (it: RuiFragment<TestNode>) -> Unit = {  }
//
//    override val fragment: RuiFragment<TestNode>
//
//    var v0: Int = 1
//
//    var ruiDirty0 = 0
//
//    fun ruiInvalidate0(mask: Int) {
//        ruiDirty0 = ruiDirty0 or mask
//    }
//
//    fun ruiEp0(it: RuiFragment<TestNode>) {
//        it as RuiT1
//        if (ruiDirty0 and 1 != 0) {
//            it.p0 = v0 + 10
//            ruiInvalidate0(1)
//        }
//    }
//
//    fun ruiEp1(it: RuiFragment<TestNode>) {
//        it as RuiT1
//        if (ruiDirty0 and 1 != 0) {
//            it.p0 = v0 + 20
//            ruiInvalidate0(1)
//        }
//    }
//
//    fun ruiBranch0(): RuiFragment<TestNode> = RuiT1(ruiAdapter, this, ::ruiEp0, v0 + 10)
//    fun ruiBranch1(): RuiFragment<TestNode> = RuiT1(ruiAdapter, this, ::ruiEp1, v0 + 20)
//    fun ruiBranch2(): RuiFragment<TestNode> = RuiPlaceholder(ruiAdapter)
//
//    fun ruiSelect(): Int =
//        when (v0) {
//            1 -> 0 // index in RuiWhen.fragments
//            2 -> 1
//            else -> 2
//        }
//
//    init {
//        fragment = RuiWhen(
//            ruiAdapter,
//            ::ruiSelect,
//            ::ruiBranch0,
//            ::ruiBranch1,
//            ::ruiBranch2
//        )
//    }
//}