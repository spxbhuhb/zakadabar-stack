/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.inline

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.field.ZkBooleanField
import zakadabar.core.browser.field.ZkStringSelectField
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.EntityId
import zakadabar.core.util.default

class TableEditInline : ZkTable<ExampleBo>() {

    val values = (1..100).map { "$it" to "option $it" }

    override fun onConfigure() {
        super.onConfigure()

        + ExampleBo::stringValue

        + custom {
            label = "Inline Checkbox"
            render = {
                + ZkBooleanField(this@TableEditInline, it::booleanValue)
            }
        } size "min-content"

        + custom {
            label = "Inline Select"
            render = {
                + ZkStringSelectField(this@TableEditInline, it::stringSelectValue).apply {
                    fetch = { values }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.smallInlineTable

        val template = default<ExampleBo> { }
        setData((1..50).map { template.copy(id = EntityId(it.toLong()), stringValue = "row $it", booleanValue = (it % 2 == 0)) })

    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}