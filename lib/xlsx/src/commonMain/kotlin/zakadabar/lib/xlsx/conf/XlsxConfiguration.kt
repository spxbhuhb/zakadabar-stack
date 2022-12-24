/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

import kotlinx.datetime.TimeZone
import zakadabar.core.resource.ZkBuiltinStrings
import zakadabar.core.resource.localizedStrings

data class XlsxConfiguration(

    val formats: XlsxFormats = XlsxFormats(),
    var strings : ZkBuiltinStrings = localizedStrings,
    var timeZone : TimeZone = TimeZone.currentSystemDefault(),
    var localizedEnums : Boolean = false,
    var localizedBooleans : Boolean = false,
    var dateFormat : XlsxFormats.NumberFormat = formats.BUILT_IN_DATE,
    var dateTimeFormat : XlsxFormats.NumberFormat = formats.BUILT_IN_DATETIME,
    var instantFormat : XlsxFormats.NumberFormat = formats.ISO_DATETIME_MILLISEC

)