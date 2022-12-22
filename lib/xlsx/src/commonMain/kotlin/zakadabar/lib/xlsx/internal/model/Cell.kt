/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.dom.*

internal class Cell(r: String, v: Any, t: Type, s: Int) : Node("c") {

    enum class Type(val code: String?) {
        NORMAL(null),
        SHARED_STRING("s"),
        STRING("str"),
        BOOLEAN("b"),
        ISO_DATE("d")
    }

    init {
        attributes["r"] = r // coordinate eg: B5

        t.code?.let { attributes["t"] = it } // type: default is normal

        if (s != 0) { // numeric format index default: 0
            attributes["s"] = s.toString()
        }

        + Node("v", v.toString())
    }

}
