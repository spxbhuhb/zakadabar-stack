/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class Cell(r: String, v: Any, t: String?, s: Int) : SimpleDomElement("c") {

    init {
        attributes["r"] = r // coordinate eg: B5

        t?.let { attributes["t"] = t } // type: default is ormal

        if (s != 0) { // numeric format index default: 0
            attributes["s"] = s.toString()
        }

        childNodes += SimpleDomElement("v", v.toString()) // cell value in internal format
    }

}
