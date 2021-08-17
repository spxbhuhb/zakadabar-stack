/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.persistence.exposed

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.core.data.EntityBo

/**
 * Exposed table used by generated tables. This layer is here so we can extend
 * the functionality later if we want.
 */
abstract class ExposedPaTable<T : EntityBo<T>>(tableName: String) : LongIdTable(tableName)