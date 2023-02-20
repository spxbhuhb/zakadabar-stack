/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.conf

import kotlinx.datetime.TimeZone
import zakadabar.core.resource.ZkBuiltinStrings
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.xlsx.model.XlsxCellFormat

/**
 * Xlsx customizable settings holder
 */
data class XlsxConfiguration(

    /**
     * Numeric formats holder
     *
     * creating new format:
     *
     *  val customFormat1 = formats.newCustomNumberFormat(formatCode)
     *  val customFormat2 = formats.newBuiltInNumberFormat(numFmtId)
     *
     */
    val formats: XlsxFormats = XlsxFormats(),

    /**
     * strings for Enum and Boolean localizations
     *
     * default: zakadabar.core.resource.localizedStrings
     */
    var strings : ZkBuiltinStrings = localizedStrings,

    /**
     *  Timezone for Instant object
     *
     *  default: TimeZone.currentSystemDefault()
     */
    var timeZone : TimeZone = TimeZone.currentSystemDefault(),

    /**
     *  true if Enums localized by strings
     *  default: false
     */
    var localizedEnums : Boolean = false,

    /**
     *  true if Booleans localized by strings
     *
     *  default: false
     */
    var localizedBooleans : Boolean = false,

    /**
     *  date format for LocalDate
     *
     *  default: formats.BUILT_IN_DATE
     */
    var dateFormat : XlsxCellFormat = formats.BUILT_IN_DATE,

    /**
     *  date format for LocalDateTime
     *
     *  default: formats.BUILT_IN_DATETIME
     */
    var dateTimeFormat : XlsxCellFormat = formats.BUILT_IN_DATETIME,

    /**
     *  date format for Instant
     *
     *  default: formats.ISO_DATETIME_MILLISEC
     */
    var instantFormat : XlsxCellFormat = formats.ISO_DATETIME_MILLISEC

)