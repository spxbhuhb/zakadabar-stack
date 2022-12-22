/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.dom.*

internal class Cell(r: String, v: Any, t: String?, s: Int) : Node("c") {

    init {
        attributes["r"] = r // coordinate eg: B5

        t?.let { attributes["t"] = t } // type: default is normal

        if (s != 0) { // numeric format index default: 0
            attributes["s"] = s.toString()
        }

        add("v", v.toString())
    }

}
