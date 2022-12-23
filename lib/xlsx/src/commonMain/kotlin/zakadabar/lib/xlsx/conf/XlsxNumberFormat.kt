/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

enum class XlsxNumberFormat(
    internal val formatCode: String?,
    internal val numFmtId: Int? = null
) {
    GENERAL(null, 0),// general must be the 1st!
    BUILT_IN_DATE(null, 14),
    BUILT_IN_DATETIME(null, 22),
    ISO_DATE("yyyy-mm-dd"),
    ISO_DATETIME_MIN("yyyy-mm-dd hh:mm"),
    ISO_DATETIME_SEC("yyyy-mm-dd hh:mm:ss"),
    ISO_DATETIME_MILLISEC("yyyy-mm-dd hh:mm:ss.000"),
}
