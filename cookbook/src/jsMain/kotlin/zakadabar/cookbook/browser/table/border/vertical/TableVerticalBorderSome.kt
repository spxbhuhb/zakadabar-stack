/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.border.vertical

import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.util.PublicApi

class ExampleStyles : ZkTableStyles() {

    // We don't want to rewrite the whole cell class, it is better to
    // use onConfigure and change these rules.

    override fun onConfigure() {
        cell.borderRight = theme.fixBorder
    }

    // This adds the border to the last header cell, so it is
    // separated from the scroll area.

    @PublicApi
    val lastHeaderCell by cssClass({ ".$table th:last-child"}) {
        borderRight = theme.fixBorder
    }
}

val exampleStyles by cssStyleSheet(ExampleStyles())

class TableVerticalBorderSome : ZkTable<ExampleBo>() {

    override var styles: ZkTableStyles = exampleStyles

    override fun onConfigure() {
        super.onConfigure()

        + ExampleBo::stringValue size 10.em
        + ExampleBo::booleanValue size 1.fr

        + actions()
    }

    override fun onCreate() {
        super.onCreate()
        demoData()
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}