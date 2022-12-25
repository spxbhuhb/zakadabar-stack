/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.toColumnNumber

data class XlsxSheet internal constructor(
    val name : String,
    val doc: XlsxDocument
) {

    private val data = mutableMapOf<Int, MutableMap<Int, XlsxCell>>()
    val columns = Columns()

    val cells : Sequence<XlsxCell> get() = data.entries
        .asSequence()
        .sortedBy { it.key }
        .flatMap { row->
            row.value.entries
                .asSequence()
                .sortedBy { it.key }
                .map { it.value }
        }

    init {
        validateName()
    }

    operator fun get(colNumber: Int, rowNumber: Int) : XlsxCell = data
        .getOrPut(rowNumber) { mutableMapOf() }
        .getOrPut(colNumber) { XlsxCell(this, XlsxCoordinate(colNumber, rowNumber)) }

   operator fun get(coordinate: XlsxCoordinate) : XlsxCell = data
        .getOrPut(coordinate.rowNumber) { mutableMapOf() }
        .getOrPut(coordinate.colNumber) { XlsxCell(this, coordinate) }

    operator fun get(coordinate: String) = get(XlsxCoordinate(coordinate))

    private fun validateName() {
        if (name.isBlank()) throw IllegalArgumentException("Worksheet name is blank.")
        if (name.length > 31) throw IllegalArgumentException("Worksheet name length exceeds 31. $name")
        if (BANNED_CHARS.containsMatchIn(name)) throw IllegalArgumentException("Worksheet name contains illegal characters. $name")
    }

    class Columns {

        internal val data = mutableMapOf<Int, Column>()
        operator fun get(columnNumber: Int) : Column = data.getOrPut(columnNumber, ::Column)
        operator fun get(columnLetter: String) : Column = get(columnLetter.toColumnNumber())

    }

    data class Column(var width: Double?= null)


    companion object {
        private var BANNED_CHARS = "[:/?*\\[\\]]".toRegex()
    }
}
