/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import org.jetbrains.exposed.sql.select

class LocaleExposedPa : LocaleExposedPaGen() {

    fun byName(name : String) =
        table
            .select { table.name eq name }
            .map { it.toBo() }
            .firstOrNull()

}