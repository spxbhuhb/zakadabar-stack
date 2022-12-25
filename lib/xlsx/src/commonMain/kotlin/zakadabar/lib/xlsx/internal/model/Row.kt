/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node

internal class Row(val rowNumber: Int) : Node("row") {

    init {
        attributes["r"] = rowNumber.toString()
    }

    fun addCell(cell: Cell) = + cell

}
