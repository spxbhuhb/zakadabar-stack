/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.cookbook.entity.builtin.ExampleReferenceBl
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.authorize.AccountBlProvider
import zakadabar.core.authorize.SimpleRoleAuthorizerProvider
import zakadabar.core.data.EntityId
import zakadabar.core.module.modules
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.util.ContentBackend
import zakadabar.core.util.PublicApi
import zakadabar.lib.examples.backend.builtin.BuiltinBl
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.lib.lucene.business.LuceneBl
import zakadabar.lib.lucene.data.UpdateIndex
import zakadabar.site.backend.business.RecipeBl

@PublicApi
object Module : RoutedModule {

    override fun onModuleLoad() {
        modules += ContentBackend("content")
        modules += RecipeBl()

        modules += SimpleRoleAuthorizerProvider {
            all = PUBLIC
        }

        // these are from lib:examples
        modules += SimpleExampleBl()
        modules += BuiltinBl()
        modules += zakadabar.lib.examples.backend.builtin.ExampleReferenceBl()

        zakadabar.lib.accounts.install()
        zakadabar.lib.lucene.install()
        zakadabar.cookbook.install()
    }

    override fun onModuleStart() {

        // Create example BOs, so CRUDs will contains some data

        transaction {
            val pa = modules.first<ExampleReferenceBl>().pa

            if (pa.list().size < 10) {
                (1..10).forEach {
                    pa.create(ExampleReferenceBo(EntityId(), "ref $it"))
                }
            }
        }

    }

    override fun onAfterOpen() {
        super.onAfterOpen()

        // Perform a Lucene index update

        val executor = modules.first<AccountBlProvider>().executorFor("so")
        modules.first<LuceneBl>().updateIndex(executor, UpdateIndex())
    }
}