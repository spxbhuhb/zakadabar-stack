import zakadabar.lib.xlsx.generateZip
import zakadabar.lib.xlsx.internal.toContentMap
import zakadabar.lib.xlsx.internal.toXlsxFile
import zakadabar.lib.xlsx.model.XlsxCoordinate
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet

fun XlsxDocument.buildFileContent(content: ByteArray.()->Unit) {
    val f = toXlsxFile()
    val cm = f.toContentMap()
    cm.generateZip(content)
}

fun XlsxSheet.fillRow(coord: String, values: Iterable<Any?>) {

    val c = XlsxCoordinate(coord)

    val rn = c.rowNumber
    var cn = c.colNumber

    values.forEach {
        this[cn, rn].value = it
        cn++
    }

}

fun XlsxSheet.fillTable(coord: String, table: Iterable<Iterable<Any?>>) {

    val c = XlsxCoordinate(coord)

    var rn = c.rowNumber

    table.forEach { row->
        var cn = c.colNumber
        row.forEach {
            this[cn, rn].value = it
            cn++
        }
        rn++
    }

}

