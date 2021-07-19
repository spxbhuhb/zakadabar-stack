/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.sql

import org.jetbrains.exposed.sql.Table
import zakadabar.stack.data.builtin.settings.DatabaseSettingsBo

interface SqlProvider {
    val tables : MutableList<Table>
    fun onCreate(config: DatabaseSettingsBo)
    fun onStart()
}