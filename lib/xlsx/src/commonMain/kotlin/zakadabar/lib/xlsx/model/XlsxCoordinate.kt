/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.internal.toColumnLetter
import zakadabar.lib.xlsx.internal.toColumnNumber

/**
 * Immutable object, to hold a coordinate within a sheet
 */
class XlsxCoordinate {

    val coordinate: String
    val rowNumber: Int
    val colNumber: Int
    val colLetter: String

    constructor(columnNumber : Int, rowNumber : Int) {
        this.rowNumber = rowNumber
        this.colNumber = columnNumber
        this.colLetter = columnNumber.toColumnLetter()
        this.coordinate = "$colLetter$rowNumber"

        validate()
    }

    constructor(coordinate: String) {

        val g = splitter.matchEntire(coordinate)?.groupValues ?: throw IllegalArgumentException("not valid coordinate: $coordinate")

        this.coordinate = coordinate
        this.colLetter = g[1]
        this.rowNumber = g[2].toInt()
        this.colNumber = colLetter.toColumnNumber()

        validate()
    }

    override fun toString() = coordinate

    private fun validate() {
        if (rowNumber < 1 || colNumber < 1) throw IllegalArgumentException("not valid coordinate: $coordinate")
    }

    companion object {
        private val splitter = "([A-Z]+)([0-9]+)".toRegex()
    }

}
