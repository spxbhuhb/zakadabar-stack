import zakadabar.lib.xlsx.model.XlsxCoordinate
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet

/**
 * Save or download xlsx file.
 */
expect fun XlsxDocument.save(fileName: String)

/**
 * fill a row width list, started at specified coordinate
 */
fun XlsxSheet.fillRow(coord: String, values: Iterable<Any?>) {

    val c = XlsxCoordinate(coord)

    val rn = c.rowNumber
    var cn = c.colNumber

    for(v in values) {
        this[cn, rn].value = v
        cn++
    }

}

/**
 * fill a table width list of lists, started at specified coordinate
 */
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
