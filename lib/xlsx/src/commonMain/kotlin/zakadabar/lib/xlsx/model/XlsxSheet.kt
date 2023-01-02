/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

import zakadabar.lib.xlsx.internal.toColumnNumber

/**
 * Xlsx Sheet object within a Document
 *
 *
 * @property columns WorkSheet Columnet settings
 * @property cells ordered list of used cells
 *
 */

data class XlsxSheet internal constructor(
    val name : String,
    val doc: XlsxDocument
) {

    private val data = mutableMapOf<Int, MutableMap<Int, XlsxCell>>()

    /**
     * Colum related settings
     */
    val columns = Columns()

    /**
     * Ordered list of used cells
     */
    val cells : Sequence<XlsxCell> get() = data.entries
        .asSequence()
        .sortedBy { it.key }
        .flatMap { row->
            row.value.entries
                .asSequence()
                .sortedBy { it.key }
                .map { it.value }
        }

    val minRowNumber : Int
        get() = data.keys.min()
    val maxRowNumber : Int
        get() = data.keys.max()

    val minColumnNumber: Int
        get() = data.values.flatMap { it.keys }.max()

    val maxColumnNumber: Int
        get() = data.values.flatMap { it.keys }.max()

    init {
        validateName()
    }

    /**
     * Access a cell object by numeric coordinates.
     * column first then row.
     * numbers start at 1
     */
    operator fun get(colNumber: Int, rowNumber: Int) : XlsxCell = data
        .getOrPut(rowNumber) { mutableMapOf() }
        .getOrPut(colNumber) { XlsxCell(this, XlsxCoordinate(colNumber, rowNumber)) }

   internal operator fun get(coordinate: XlsxCoordinate) : XlsxCell = data
        .getOrPut(coordinate.rowNumber) { mutableMapOf() }
        .getOrPut(coordinate.colNumber) { XlsxCell(this, coordinate) }

    /**
     * Access a cell object by text coordinate
     * eg: A2, BA34, Q4
     */
    operator fun get(coordinate: String) = get(XlsxCoordinate(coordinate))

    private fun validateName() {
        if (name.isBlank()) throw IllegalArgumentException("Worksheet name is blank.")
        if (name.length > 31) throw IllegalArgumentException("Worksheet name length exceeds 31. $name")
        if (BANNED_CHARS.containsMatchIn(name)) throw IllegalArgumentException("Worksheet name contains illegal characters. $name")
    }

    /**
     * Column settings container
     */
    class Columns {

        internal val data = mutableMapOf<Int, Column>()

        /**
         * Access Column settings by number (first column is 1)
         */
        operator fun get(columnNumber: Int) : Column = data.getOrPut(columnNumber, ::Column)

        /**
         * Access Column settings by letter (first column is A)
         */
        operator fun get(columnLetter: String) : Column = get(columnLetter.toColumnNumber())

    }

    /**
     * a Column settings
     */
    data class Column(
        /**
         * custom Column width
         */
        var width: Double?= null
    )


    companion object {
        private var BANNED_CHARS = "[:/?*\\[\\]]".toRegex()
    }
}
