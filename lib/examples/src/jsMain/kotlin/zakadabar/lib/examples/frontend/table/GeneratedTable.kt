/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.table

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.frontend.crud.BuiltinTable
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.util.default

/**
 * This example shows all built in table columns with generated table data.
 */
object GeneratedTable : ZkPage(css = zkPageStyles.fixed) {

    override fun onCreate() {
        super.onCreate()

        // Create a template DTO.

        val template: BuiltinBo = default { }

        // Create the data to display. "default" uses the schema to generate the
        // default data therefore it is slow. Using "copy" is better suited for
        // large record count.

        val data = (1..10000).map { template.copy(id = EntityId(it.toLong())) }

        // Add the table and set the data. It is not important to use these
        // together, you can add the table and set the data later.

        + BuiltinTable().setData(data)
    }
}