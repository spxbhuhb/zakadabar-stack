/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import zakadabar.lib.xlsx.model.XlsxDocument

internal fun String.toColumnNumber() : Int {
    var col = 0
    var n = length
    var k = 1
    while(--n >= 0) {
        col += (1 + this[n].code - 65) * k
        k *= 26
    }
    return col
}

internal fun Int.toColumnLetter() : String {
    var k = this
    val col = StringBuilder()
    while(k-- > 0) {
        col.insert(0, (k % 26 + 65).toChar())
        k /= 26
    }
    return col.toString()
}

/**
 * Convert public model to internal dom, then write it into stream
 */
internal fun <T> XlsxDocument.buildFileContent(zipContent: ContentWriter<T>) {
    val f = toXlsxFile()
    val c = f.toContentMap()
    c.generateZip(zipContent)
}
