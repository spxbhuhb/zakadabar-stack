/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

class XlsxCoordinate : Comparable<XlsxCoordinate> {

    val coordinate: String
    val rowNumber: Int
    val colNumber: Int
    val colLetter: String

    constructor(rowNumber : Int, colNumber : Int) {
        this.rowNumber = rowNumber
        this.colNumber = colNumber
        this.colLetter = colNumber.toXlsxColumnLetter()
        this.coordinate = "$colLetter$rowNumber"

        validate()
    }

    constructor(coordinate: String) {

        val g = splitter.matchEntire(coordinate)?.groupValues ?: throw IllegalArgumentException("not valid coordinate: $coordinate")

        this.coordinate = coordinate
        this.colLetter = g[1]
        this.rowNumber = g[2].toInt()
        this.colNumber = colLetter.toXlsxColumnNumber()

        validate()
    }

    override fun toString() = coordinate

    override fun hashCode(): Int = coordinate.hashCode()

    override fun equals(other: Any?): Boolean {
        val o = other as? XlsxCoordinate ?: return false
        return rowNumber == o.rowNumber && colNumber == o.colNumber
    }

    override fun compareTo(other: XlsxCoordinate): Int = when {
        rowNumber > other.rowNumber -> 1
        rowNumber < other.rowNumber -> -1
        colNumber > other.colNumber -> 1
        colNumber < other.colNumber -> -1
        else -> 0
    }

    private fun validate() {
        if (rowNumber < 1 || colNumber < 1) throw IllegalArgumentException("not valid coordinate: $coordinate")
    }

    companion object {
        private val splitter = "([A-Z]+)([0-9]+)".toRegex()
    }

}
