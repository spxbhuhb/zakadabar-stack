/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.table

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.frontend.crud.BuiltinTable
import zakadabar.core.frontend.builtin.pages.ZkPage
import zakadabar.core.frontend.builtin.pages.zkPageStyles
import zakadabar.core.frontend.util.io

/**
 * This example shows all built in table columns with generated table data.
 */
object FetchedTable : ZkPage(css = zkPageStyles.fixed) {

    override fun onCreate() {
        super.onCreate()
        io {
            // Add the table and set the data. It is not important to use these
            // together, you can add the table and set the data later.

            + BuiltinTable().setData(BuiltinBo.all())
        }
    }

}