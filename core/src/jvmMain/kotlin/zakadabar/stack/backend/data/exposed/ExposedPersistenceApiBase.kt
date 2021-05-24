/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.data.exposed

import org.jetbrains.exposed.sql.transactions.TransactionManager

interface ExposedPersistenceApi {

    fun exposedCommit() {
        TransactionManager.current().commit()
    }

    fun exposedRollback() {
        TransactionManager.current().rollback()
    }

}