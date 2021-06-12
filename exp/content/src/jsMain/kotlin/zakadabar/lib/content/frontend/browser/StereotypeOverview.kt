/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.StereotypeBo
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.columns.ZkCustomColumn
import zakadabar.stack.frontend.util.io

class StereotypeOverview : ZkPage(css = zkPageStyles.fixed) {

    override fun onConfigure() {
        setAppTitle = false // let the table do it
    }

    override fun onResume() {
        super.onResume()
        io {
            + StereotypeOverviewTable(
                LocaleBo.all(),
                StereotypeBo.all(),
            )
        }
    }

    override fun onPause() {
        this.clear()
    }

}

class StereotypeOverviewTable(
    private val locales: List<LocaleBo>,
    private val stereotypes: List<StereotypeBo>
) : ZkTable<StereotypeBo>() {

    override fun onConfigure() {

        add = true
        search = true
        export = true

        + StereotypeBo::id
        + StereotypeBo::name
        + StereotypeBo::parent

        locales.forEach { locale ->
            + LocaleColumn(this, locale.id,).apply { label = locale.name }
        }

        + actions()
    }

    override fun onResume() {
        super.onResume()
        setData(stereotypes)
    }

    class LocaleColumn(
        table: ZkTable<StereotypeBo>,
        private val localeId: EntityId<LocaleBo>
    ) : ZkCustomColumn<StereotypeBo>(
        table = table
    ) {
        override fun render(builder: ZkElement, index: Int, row: StereotypeBo) {
            with(builder) {
                + ZkCheckBox(
                    readOnly = true,
                    checked = (row.localizations.firstOrNull { it.locale == localeId } != null)
                )
            }
        }
    }
}

