/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.basic

import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.util.default

class BasicTableDirect : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        export = true

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr
    }

    override fun onCreate() {
        super.onCreate()

        val template = default<ExampleBo> { }

        val data = (1..50).map {
            template.copy(id = EntityId(it.toLong()), stringValue = "row $it", booleanValue = (it % 2 == 0))
        }

        setData(data)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}


