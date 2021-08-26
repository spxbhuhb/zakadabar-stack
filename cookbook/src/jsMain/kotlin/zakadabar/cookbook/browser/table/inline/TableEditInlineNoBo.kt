/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.inline

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.table.columns.ZkBooleanExtensionColumn
import zakadabar.core.browser.table.columns.ZkStringExtensionColumn
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.fr
import zakadabar.core.util.default

class TableEditInlineNoBo : ZkTable<ExampleBo>() {

    val values = (1..100).map { "$it" to "option $it" }

    val boxes = ZkBooleanExtensionColumn(this, "Box", ::onChangeBox)
    val boxes2 = ZkBooleanExtensionColumn(this, "Box", ::onChangeBox)
    val strings = ZkStringExtensionColumn(this, "String", ::onChangeString)

    override fun onConfigure() {
        super.onConfigure()

        + ExampleBo::stringValue size 1.fr
        + boxes size "min-content"
        + boxes2 size "min-content"
        + strings size "min-content"

    }

    private fun onChangeBox(row: ExampleBo, value: Boolean) {
        toastSuccess { "box for ${row.stringValue} changed to $value" }
    }

    private fun onChangeString(row: ExampleBo, value: String) {
        toastSuccess { "text for ${row.stringValue} changed to $value" }
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