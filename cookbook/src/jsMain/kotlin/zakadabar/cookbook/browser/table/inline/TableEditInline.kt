/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.inline

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.form.fields.ZkBooleanField
import zakadabar.stack.frontend.builtin.form.fields.ZkStringSelectField
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.zkTableStyles
import zakadabar.stack.util.default

class TableEditInline : ZkTable<ExampleBo>() {

    val values = (1..100).map { "$it" to "option $it" }

    override fun onConfigure() {
        super.onConfigure()

        + ExampleBo::stringValue

        + custom {
            label = "Inline Checkbox"
            render = {
                + zkTableStyles.dense
                + ZkBooleanField(this@TableEditInline, it::booleanValue)
            }
        }

        + custom {
            label = "Inline Select"
            render = {
                + zkTableStyles.dense
                + ZkStringSelectField(this@TableEditInline, it::stringSelectValue, false) { values }
            }
        }

        actions()
    }

    override fun onCreate() {
        super.onCreate()

        val template = default<ExampleBo> { }
        setData((1..50).map { template.copy(id = EntityId(it.toLong()), stringValue = "string $it", booleanValue = (it % 2 == 0)) })

    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}