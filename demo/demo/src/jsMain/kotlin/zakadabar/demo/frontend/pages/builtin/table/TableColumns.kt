/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.builtin.table

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.plusAssign

/**
 * This example shows all built in table columns.
 */
object TableColumns : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        // this makes the form to fill the whole page
        classList += grow

        val table = Table()
        val data = (1..1000).map {
            val dto: BuiltinDto = default { id = it.toLong() }
            dto
        }

        + table
        table.setData(data)
    }

    class Table : ZkTable<BuiltinDto>() {

        override fun onConfigure() {
            title = "Example Table"

            add = true
            search = true
            export = true

            + BuiltinDto::id
            + BuiltinDto::booleanValue
            + BuiltinDto::doubleValue
            + BuiltinDto::enumSelectValue
            + BuiltinDto::instantValue
            + BuiltinDto::stringValue
        }

    }

}