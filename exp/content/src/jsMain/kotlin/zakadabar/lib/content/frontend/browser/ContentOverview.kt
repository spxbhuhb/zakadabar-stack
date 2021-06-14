/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import zakadabar.lib.content.data.ContentOverview
import zakadabar.lib.content.data.ContentOverviewEntry
import zakadabar.lib.content.data.ContentOverviewQuery
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.input.ZkCheckBox
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.builtin.table.columns.ZkCustomColumn
import zakadabar.stack.frontend.resources.css.em
import zakadabar.stack.frontend.util.io

class ContentOverview : ZkPage(css = zkPageStyles.fixed) {

    override fun onConfigure() {
        setAppTitle = false // let the table do it
    }

    override fun onResume() {
        super.onResume()
        io {
            + ContentOverviewTable(ContentOverviewQuery().execute())
        }
    }

    override fun onPause() {
        this.clear()
    }

}

class ContentOverviewTable(
    private val overview: ContentOverview
) : ZkTable<ContentOverviewEntry>() {

    override fun onConfigure() {

        add = true
        search = true
        export = true

        + ContentOverviewEntry::id size 6.em
        + ContentOverviewEntry::path
        + ContentOverviewEntry::status size 10.em

        overview.locales.forEachIndexed { index, locale ->
            + LocaleColumn(this, locale.id, index).apply { label = locale.name } size 3.em
        }

        + actions() size 6.em

    }

    override fun onResume() {
        super.onResume()
        setData(overview.entries.sortedBy { it.path })
    }

    override fun onAddRow() {
        target<ContentEditor>().openCreate()
    }

    override fun onDblClick(id: String) {
        target<ContentEditor>().openUpdate(EntityId(id))
    }

    override fun getRowId(row: ContentOverviewEntry) = row.id.value

    class LocaleColumn(
        table: ZkTable<ContentOverviewEntry>,
        private val localeId: EntityId<LocaleBo>,
        private val localeIndex: Int
    ) : ZkCustomColumn<ContentOverviewEntry>(
        table = table
    ) {
        override fun render(builder: ZkElement, index: Int, row: ContentOverviewEntry) {
            with(builder) {

                val localizedId = row.localizations[localeIndex]

                + ZkCheckBox(readOnly = true, checked = localizedId != null)
                    .on("dblclick") { event ->

                        event.stopPropagation() // to stop table double click

                        if (localizedId != null) {
                            target<ContentEditor>().openUpdate(localizedId)
                        } else {
                            target<ContentEditor>().openCreate(ContentEditor.Args(row.id, localeId))
                        }
                    }
            }
        }
    }
}

