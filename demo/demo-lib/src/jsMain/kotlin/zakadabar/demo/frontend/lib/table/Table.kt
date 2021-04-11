/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.lib.table

import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.frontend.lib.crud.BuiltinTable
import zakadabar.stack.frontend.builtin.layout.ZkLayoutStyles.grow
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.plusAssign
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * This example shows all built in table columns with generated table data.
 */
object Table : ZkPage() {

    @ExperimentalTime
    override fun onCreate() {
        super.onCreate()

        // this makes the form to fill the whole page
        classList += grow

        val b: BuiltinDto = default { }
        val table = BuiltinTable()
        val data = (1..100).map { b.copy(id = it.toLong()) }
        + table
        table.setData(data)

    }

}