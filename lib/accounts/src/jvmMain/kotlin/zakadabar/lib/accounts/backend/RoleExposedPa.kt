/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import org.jetbrains.exposed.sql.select

class RoleExposedPa : RoleExposedPaGen() {

    fun readByName(name: String) =
        RoleExposedTable
        .select { RoleExposedTable.name eq name }
            .first()
            .let { it.toBo() }

}