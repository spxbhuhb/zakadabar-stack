/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.counter

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.resource.css.px

class TableWithCounter : ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        height = 400.px

        addLocalTitle = true
        search = true
        counter = true
//        allCount = 20

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

    override fun onCreate() {
        super.onCreate()
        demoData()
    }

}