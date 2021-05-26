/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.query

import kotlinx.datetime.internal.JSJoda.DateTimeFormatter
import zakadabar.lib.examples.data.builtin.ExampleResult
import zakadabar.stack.frontend.builtin.table.ZkTable

class ResultTable : ZkTable<ExampleResult>() {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    override fun onConfigure() {
        super.onConfigure()

        search = true
        export = true

        + ExampleResult::EntityId
        + ExampleResult::booleanValue
        + ExampleResult::enumSelectValue
        + ExampleResult::intValue
        + ExampleResult::stringValue
    }

    override fun getRowId(row: ExampleResult): String {
        return row.EntityId.toString()
    }
}