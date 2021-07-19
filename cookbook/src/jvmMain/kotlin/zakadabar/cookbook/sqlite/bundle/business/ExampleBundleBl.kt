/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle.business

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.cookbook.sqlite.bundle.ExampleBundle
import zakadabar.cookbook.sqlite.bundle.persistence.ExampleBundlePa
import zakadabar.cookbook.sqlite.bundle.persistence.ExamplePa
import zakadabar.cookbook.sqlite.bundle.persistence.ExampleTable
import zakadabar.lib.blobs.sqlite.buildBundle
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.util.UUID

class ExampleBundleBl : EntityBusinessLogicBase<ExampleBundle>(
    boClass = ExampleBundle::class
) {

    override val pa = ExampleBundlePa()

    override val authorizer by provider()

    override fun create(executor: Executor, bo: ExampleBundle): ExampleBundle {

        val fileName = "./app/var/${UUID()}.db"
        val tables = listOf(ExampleTable)

        val examplePa = ExamplePa()

        bo.content = buildBundle(fileName, tables) { db ->

            // uses the default database
            val bos = transaction {
                examplePa.list()
            }

            transaction(db) {
                bos.forEach {
                    examplePa.create(it)
                }
            }
        }

        return super.create(executor, bo)
    }

}