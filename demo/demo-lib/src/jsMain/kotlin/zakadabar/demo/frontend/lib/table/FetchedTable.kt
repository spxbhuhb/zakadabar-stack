/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.table

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.frontend.lib.crud.BuiltinTable
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

/**
 * This example shows all built in table columns with generated table data.
 */
object FetchedTable : ZkPage(cssClasses = arrayOf(grow)) {

    override fun onCreate() {
        super.onCreate()
        io {
            // Add the table and set the data. It is not important to use these
            // together, you can add the table and set the data later.

            + BuiltinTable().setData(BuiltinDto.all())
        }
    }

}