/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.Component

fun Branch(num: Int) {
    when (num) {
        1 -> Text("one")
        2 -> Text("two")
    }
}

fun ReactiveBranch(num: Int): Component {

    var num0 = num

    var dirty0 = 0
    val self0 = Component()

    var branch0: Component? = null
    var branch0idx = - 1

    val branch0funs = arrayOf(
        fun(): Component {
            val self1 = Component()

            var text0: Text? = null

            self1.c = {
                text0 = Text("one")
            }

            self1.p = {
                text0!!.setValue("one")
            }

            return self1
        },
        fun(): Component {
            val self1 = Component()

            var text0: Text? = null

            self1.c = {
                text0 = Text("two")
            }

            self1.p = {
                text0!!.setValue("one")
            }

            return self1
        }
    )

    fun branch0select() : Int {
        return when (num0) {
            1 -> 0
            2 -> 1
            else -> -1
        }
    }

    self0.c = {
        branch0idx = branch0select()
        if (branch0idx != -1) {
            branch0 = branch0funs[branch0idx]()
            branch0!!.c()
        }
    }

    self0.p = { dirty ->
        dirty0 = dirty
        if (dirty and 1 != 0) {
            val branch0future = branch0select()
            if (branch0future == branch0idx) {
                branch0?.p?.invoke(0)
            } else {
                // unmount, destroy
                branch0idx = branch0future
                if (branch0idx != -1) {
                    branch0 = branch0funs[branch0idx]()
                    branch0!!.c()
                    // mount
                }
            }
        }
    }

    self0.s = { mask, value ->
        when (mask) {
            1 -> num0 = value as Int
        }
        self0.p(mask)
    }

    return self0
}