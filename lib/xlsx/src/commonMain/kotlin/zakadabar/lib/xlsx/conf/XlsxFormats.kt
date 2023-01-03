/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

import zakadabar.lib.xlsx.internal.BuiltInNumberFormat
import zakadabar.lib.xlsx.internal.CustomNumberFormat
import zakadabar.lib.xlsx.model.XlsxCellFormat

/**
 * Hold cell formats with some common preset
 */
class XlsxFormats {

    internal val numberFormats = mutableListOf<XlsxCellFormat>()

    /**
     * general format, aka unformatted
     */
    val GENERAL = newBuiltInNumberFormat(0)

    /**
     * xlsx built in date. it depends on client's language
     */
    val BUILT_IN_DATE = newBuiltInNumberFormat(14)

    /**
     * xlsx built in datetime. it depends on client's language
     */
    val BUILT_IN_DATETIME by lazy { newBuiltInNumberFormat(22) }

    /**
     * iso date
     */
    val ISO_DATE by lazy { newCustomNumberFormat("yyyy-mm-dd") }

    /**
     * iso datetime, minute precision
     */
    val ISO_DATETIME_MIN by lazy { newCustomNumberFormat("yyyy-mm-dd\\Thh:mm") }

    /**
     * iso datetime seconds precision
     */
    val ISO_DATETIME_SEC by lazy { newCustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss") }

    /**
     * iso datetime milliseconds precision
     */
    val ISO_DATETIME_MILLISEC by lazy { newCustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss.000") }

    /**
     * Create new NumberFormat from Built-in set
     */
    fun newBuiltInNumberFormat(numFmtId: Int) : XlsxCellFormat {
        val xfId = numberFormats.size
        val f = BuiltInNumberFormat(xfId, numFmtId)
        numberFormats += f
        return f
    }

    /**
     * Create new NumberFormat from custom format String
     */
    fun newCustomNumberFormat(formatCode: String) : XlsxCellFormat {
        val xfId = numberFormats.size
        val f = CustomNumberFormat(xfId, formatCode)
        numberFormats += f
        return f
    }

}
