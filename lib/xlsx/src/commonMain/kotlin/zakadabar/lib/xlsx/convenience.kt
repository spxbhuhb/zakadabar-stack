import zakadabar.lib.xlsx.model.XlsxCoordinate
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet

expect fun XlsxDocument.save(fileName: String)

fun XlsxSheet.fillRow(coord: String, values: Iterable<Any?>) {

    val c = XlsxCoordinate(coord)

    val rn = c.rowNumber
    var cn = c.colNumber

    for(v in values) {
        this[cn, rn].value = v
        cn++
    }

}

fun XlsxSheet.fillTable(coord: String, table: Iterable<Iterable<Any?>>) {

    val c = XlsxCoordinate(coord)

    var rn = c.rowNumber

    for(row in table) {
        var cn = c.colNumber
        for(v in row) {
            this[cn, rn].value = v
            cn++
        }
        rn++
    }

}
