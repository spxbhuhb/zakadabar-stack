/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle.business

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.cookbook.entity.builtin.ExamplePa
import zakadabar.cookbook.entity.builtin.ExampleTable
import zakadabar.cookbook.sqlite.bundle.ExampleBundle
import zakadabar.cookbook.sqlite.bundle.persistence.ExampleBundlePa
import zakadabar.lib.blobs.sqlite.buildBundle
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.util.UUID

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
                    examplePa.createWithId(it)
                }
            }
        }

        return super.create(executor, bo)
    }

}