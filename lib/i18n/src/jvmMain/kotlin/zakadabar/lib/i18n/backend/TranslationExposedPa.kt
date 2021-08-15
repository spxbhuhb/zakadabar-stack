/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import org.jetbrains.exposed.sql.select
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.core.data.StringPair
import zakadabar.core.data.EntityId

open class TranslationExposedPa : TranslationExposedPaGen() {

    fun translationsByLocale(localeId : EntityId<LocaleBo>) =
        table
            .select { table.locale eq localeId.toLong() }
            .map {
                StringPair(
                    it[table.key],
                    it[table.value]
                )
            }

}
