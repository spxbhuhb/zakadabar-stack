import zakadabar.lib.xlsx.model.XlsxCoordinate
import zakadabar.lib.xlsx.model.XlsxSheet

fun XlsxSheet.setCell(coord: String, value: Any?) {
    this[coord].value = value
}

fun XlsxSheet.setRow(coord: String, values: Iterable<Any?>) {

    val c = XlsxCoordinate(coord)

    val rn = c.rowNumber
    var cn = c.colNumber

    values.forEach {
        this[cn, rn].value = it
        cn++
    }

}

fun XlsxSheet.setTable(coord: String, table: Iterable<Iterable<Any?>>) {

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
