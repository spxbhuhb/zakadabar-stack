/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class Row(val rowNumber: Int) : SimpleDomElement("row") {

    init {
        attributes["r"] = rowNumber.toString()
    }

    fun addCell(cell: Cell) {
        childNodes += cell
    }

}
