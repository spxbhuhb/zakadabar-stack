/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.exposed

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * Exposed table used by generated tables. This layer is here so we can extend
 * the functionality later if we want.
 */
abstract class LinkedExposedPaTable(tableName: String) : Table(tableName) {
    abstract val entityId : Column<EntityID<Long>>
}