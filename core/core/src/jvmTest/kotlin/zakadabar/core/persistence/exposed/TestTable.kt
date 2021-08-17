/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence.exposed

import zakadabar.core.util.UUID

object TestTable : ExposedPaTable<TestBo>(
    tableName = UUID().toString().replace('-','_')
) {

    val name = varchar("name", 30)

}