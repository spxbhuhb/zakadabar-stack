/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.util.default
import zakadabar.core.util.fourRandomInt
import kotlin.math.min

fun ZkTable<ExampleBo>.demoData(count : Int = 50) {

    + cookbookStyles.smallInlineTable

    val template = default<ExampleBo> { }

    val data = (1..count).map {
        template.copy(id = EntityId(it.toLong()), stringValue = "row $it", booleanValue = (it % 2 == 0), doubleValue = it / 10.0)
    }

    setData(data)

}

fun ZkTable<ExampleBo>.demoDataLong(css : ZkCssStyleRule = cookbookStyles.smallInlineTable, count : Int = 50) {

    + css

    val template = default<ExampleBo> { }

    val source = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

    val data = (1..count).map {
        val stringValue = source.substring(0, min(source.length, fourRandomInt()[0] % source.length))
        template.copy(id = EntityId(it.toLong()), stringValue = "row $it $stringValue", booleanValue = (it % 2 == 0))
    }

    setData(data)

}