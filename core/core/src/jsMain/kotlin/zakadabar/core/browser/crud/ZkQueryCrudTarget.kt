/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.crud

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.newInstance
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityBo
import kotlin.reflect.KClass

/**
 * Routing target for CRUD pages which a query, create, read, update, delete.
 * the query uses table, others use form. Intended for top-level pages.
 * If you would like to include a crud on a page, use [ZkInlineQueryCrud].
 *
 * This class **does not** handle the table automatically as [ZkCrudTarget]
 * does, you have to do it yourself. See the documentation for details.
 */
@Suppress("unused") // API class
open class ZkQueryCrudTarget<T : EntityBo<T>, ET : BaseBo> : ZkCrudTarget<T>() {

    lateinit var queryTableClass: KClass<out ZkTable<ET>>

    override fun all(): ZkElement {

        val container = ZkElement()

        io {
            val table = queryTableClass.newInstance()
            container build {
                + zkPageStyles.fixed
                + table
            }
        }

        return container
    }

}
