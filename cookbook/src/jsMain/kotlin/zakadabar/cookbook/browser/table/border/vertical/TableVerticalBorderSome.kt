/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.cookbook.browser.table.border.vertical

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.table.ZkTableStyles
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.em
import zakadabar.core.resource.css.fr
import zakadabar.core.resource.css.px
import zakadabar.core.util.PublicApi
import zakadabar.core.util.default


class ExampleStyles : ZkTableStyles() {

    // We don't want to rewrite the whole cell class, it is better to
    // use onConfigure and change these rules.

    override fun onConfigure() {
        cell.borderRight = theme.fixBorder
        cell.paddingLeft = (theme.spacingStep / 2).px
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

        actions()
    }

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.smallInlineTable

        val template = default<ExampleBo> { }

        val data = (1..50).map {
            template.copy(id = EntityId(it.toLong()), stringValue = "string $it", booleanValue = (it % 2 == 0))
        }

        setData(data)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

}