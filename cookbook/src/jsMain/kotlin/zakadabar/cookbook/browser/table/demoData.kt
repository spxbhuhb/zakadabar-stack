/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.EntityId
import zakadabar.core.util.default

fun ZkTable<ExampleBo>.demoData() {

    + cookbookStyles.smallInlineTable

    val template = default<ExampleBo> { }

    val data = (1..50).map {
        template.copy(id = EntityId(it.toLong()), stringValue = "row $it", booleanValue = (it % 2 == 0))
    }

    setData(data)

}