/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource.locales

import zakadabar.core.resource.AbstractLocalizedFormats
import zakadabar.core.resource.LocalizationConfig

object DefaultLocalizedFormats : AbstractLocalizedFormats(
    LocalizationConfig(
        thousandSeparator = ",",
        decimalSeparator = "."
    )
)