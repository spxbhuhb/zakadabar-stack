/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

/**
 * Hold cell formats with some common prfeset
 */
class XlsxFormats {

    internal val numberFormats = mutableListOf<NumberFormat>()

    /**
     * general format, aka unformatted
     */
    val GENERAL = BuiltInNumberFormat(0)

    /**
     * xlsx built in date. it depends on client's language
     */
    val BUILT_IN_DATE by lazy { BuiltInNumberFormat(14) }

    /**
     * xlsx built in datetime. it depends on client's language
     */
    val BUILT_IN_DATETIME by lazy { BuiltInNumberFormat(22) }

    /**
     * iso date
     */
    val ISO_DATE by lazy { CustomNumberFormat("yyyy-mm-dd") }

    /**
     * iso datetime, minute precision
     */
    val ISO_DATETIME_MIN by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm") }

    /**
     * iso datetime seconds precision
     */
    val ISO_DATETIME_SEC by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss") }

    /**
     * iso datetime milliseconds precision
     */
    val ISO_DATETIME_MILLISEC by lazy { CustomNumberFormat("yyyy-mm-dd\\Thh:mm:ss.000") }

    abstract inner class NumberFormat {
        val xfId = numberFormats.size
    }

    /**
     * Create new NUmberFormat from Built-in set
     */
    inner class BuiltInNumberFormat(val numFmtId: Int) : NumberFormat() {
        init { numberFormats += this }
    }

    /**
     * Create new NumberFormat from custom format String
     */
    inner class CustomNumberFormat(val formatCode: String) : NumberFormat() {
        init { numberFormats += this }

    }

}
