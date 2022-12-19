/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

fun String.toXlsxColumnNumber() : Int {
    var col = 0
    var n = length
    var k = 1
    while(--n >= 0) {
        col += (1 + this[n].code - 65) * k
        k *= 26
    }
    return col
}

fun Int.toXlsxColumnLetter() : String {
    var k = this
    val col = StringBuilder()
    while(k-- > 0) {
        col.insert(0, (k % 26 + 65).toChar())
        k /= 26
    }
    return col.toString()
}

fun XlsxCoordinate.next() = XlsxCoordinate(rowNumber, colNumber + 1)

typealias ContentMap = HashMap<String, ()->ByteArray>

fun ContentMap.saveXlsx(name: String) = saveZip(name, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
expect fun ContentMap.saveZip(name: String, contentType: String = "application/zip")

fun XlsxSheet.addCell(coord: XlsxCoordinate, data: String) {
    this += XlsxCell(coord).apply {
        set(data)
    }
}
fun XlsxSheet.addCell(coord: String, data: String) = addCell(XlsxCoordinate(coord), data)

