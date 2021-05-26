/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.data

import org.jetbrains.exposed.sql.select
import zakadabar.lib.examples.data.SimpleExampleQuery
import zakadabar.lib.examples.data.SimpleQueryResult

class SimpleExampleExposedPa : SimpleExampleExposedPaGen() {

    fun query(query: SimpleExampleQuery) =
        table
            .select { table.name like query.name }
            .map {
                SimpleQueryResult(
                    name2x = it[table.name] + it[table.name]
                )
            }


}