/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.locales

import kotlinx.datetime.*
import zakadabar.core.resource.AbstractLocalizedFormats
import zakadabar.core.resource.LocalizationConfig

object HuLocalizedFormats : AbstractLocalizedFormats(
    LocalizationConfig(
        thousandSeparator = ".",
        decimalSeparator = ","
    )
) {

    override fun format(value: Instant): String {
        val local = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return local.substring(0, 10).replace("-", ".") + ". " + local.substring(11, 19)
    }

    override fun format(value: LocalDate): String {
        return value.toString().replace("-", ".") + "."
    }

    override fun format(value: LocalDateTime): String {
        val local = value.toString()
        return local.substring(0, 10).replace("-", ".") + ". " + local.substring(11, 19)
    }

}