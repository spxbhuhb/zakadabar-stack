/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

class Cell(coord: String, value: String, type: String) : SimpleDomElement("c") {

    init {
        attributes["r"] = coord
        attributes["t"] = type

        childNodes += SimpleDomElement("v", value)
    }

}
