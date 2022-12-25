/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

class XlsxFormats {

    internal val numberFormats = mutableListOf<NumberFormat>()

    val GENERAL = BuiltInNumberFormat(0)
    val BUILT_IN_DATE by lazy { BuiltInNumberFormat(14) }
    val BUILT_IN_DATETIME by lazy { BuiltInNumberFormat(22) }
    val ISO_DATE by lazy { CustomNumberFormat("yyyy-mm-dd") }
    val ISO_DATETIME_MIN by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm") }
    val ISO_DATETIME_SEC by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss") }
    val ISO_DATETIME_MILLISEC by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss.000") }

    abstract inner class NumberFormat {
        val xfId = numberFormats.size
        init { numberFormats += this }
    }

    inner class BuiltInNumberFormat(val numFmtId: Int) : NumberFormat()
    inner class CustomNumberFormat(val formatCode: String) : NumberFormat()

}
