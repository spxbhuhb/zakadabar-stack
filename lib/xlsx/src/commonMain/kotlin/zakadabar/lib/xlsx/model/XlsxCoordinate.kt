/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

class XlsxCoordinate {

    val coordinate: String
    val rowNumber: Int
    val colNumber: Int
    val colLetter: String

    constructor(columnNumber : Int, rowNumber : Int) {
        this.rowNumber = rowNumber
        this.colNumber = columnNumber
        this.colLetter = columnNumber.toXlsxColumnLetter()
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
        return other is XlsxCoordinate && rowNumber == other.rowNumber && colNumber == other.colNumber
    }

    private fun validate() {
        if (rowNumber < 1 || colNumber < 1) throw IllegalArgumentException("not valid coordinate: $coordinate")
    }

    companion object {
        private val splitter = "([A-Z]+)([0-9]+)".toRegex()
    }

    private fun String.toXlsxColumnNumber() : Int {
        var col = 0
        var n = length
        var k = 1
        while(--n >= 0) {
            col += (1 + this[n].code - 65) * k
            k *= 26
        }
        return col
    }

    private fun Int.toXlsxColumnLetter() : String {
        var k = this
        val col = StringBuilder()
        while(k-- > 0) {
            col.insert(0, (k % 26 + 65).toChar())
            k /= 26
        }
        return col.toString()
    }


}
