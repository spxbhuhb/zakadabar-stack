/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.Component

fun Button(label : String, icon : String) {
    Div {
        Text(label)
    }
}

fun ReactiveButton(label : String, icon : String): Component {

    var label0 = label
    var icon0 = icon

    var dirty0 = 0
    val self0 = Component()

    var div0: Component? = null

    val content0 = fun(): Component {
        var dirty1 = 0
        val self1 = Component()

        var text0: Text? = null

        self1.c = {
            text0 = Text(label0)
        }

        self1.p = { dirty ->
            dirty1 = dirty
            if (dirty0 and 1 != 0) text0 !!.setValue(label0)
        }

        return self1
    }

    self0.c = {
        div0 = ReactiveDiv(content0)
        div0!!.c()
    }

    self0.p = { dirty ->
        dirty0 = dirty
        if (dirty and 1 != 0) div0!!.p(1)
        if (dirty and 2 != 0) div0!!.p(1)
    }

    self0.s = { mask, value ->
        when (mask) {
            1 -> label0 = value as String
            2 -> icon0 = value as String
        }
        self0.p(mask)
    }

    return self0
}