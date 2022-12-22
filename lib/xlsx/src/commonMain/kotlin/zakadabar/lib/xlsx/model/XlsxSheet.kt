/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.model

data class XlsxSheet(val title : String) {

    lateinit var doc : XlsxDocument

    private val data = mutableMapOf<Int, MutableMap<Int, XlsxCell>>()

    val cells : Sequence<XlsxCell> get() = data.entries
        .asSequence()
        .sortedBy { it.key }
        .flatMap { row->
            row.value.entries
                .asSequence()
                .sortedBy { it.key }
                .map { it.value }
        }

    operator fun get(colNumber: Int, rowNumber: Int) : XlsxCell = data
        .getOrPut(rowNumber) { mutableMapOf() }
        .getOrPut(colNumber) { XlsxCell(this, XlsxCoordinate(colNumber, rowNumber)) }

   operator fun get(coordinate: XlsxCoordinate) : XlsxCell = data
        .getOrPut(coordinate.rowNumber) { mutableMapOf() }
        .getOrPut(coordinate.colNumber) { XlsxCell(this, coordinate) }

    operator fun get(coordinate: String) = get(XlsxCoordinate(coordinate))


}
