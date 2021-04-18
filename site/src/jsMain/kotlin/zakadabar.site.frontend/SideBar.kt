/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.demo.frontend.lib.crud.BuiltinCrud
import zakadabar.demo.frontend.lib.form.FormFields
import zakadabar.demo.frontend.lib.input.CheckboxList
import zakadabar.demo.frontend.lib.layout.TabContainer
import zakadabar.demo.frontend.lib.modal.ConfirmDialog
import zakadabar.demo.frontend.lib.pages.ArgPage
import zakadabar.demo.frontend.lib.table.FetchedTable
import zakadabar.demo.frontend.lib.table.GeneratedTable
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.site.frontend.pages.misc.ChangeLog
import zakadabar.site.frontend.pages.misc.Content
import zakadabar.site.frontend.pages.misc.GettingStarted
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.fourRandomInt

object SideBar : ZkSideBar() {

    lateinit var contents: List<ContentEntry>

    override fun onCreate() {
        super.onCreate()

        io {
            contents = ContentQuery().execute().sortedBy { it.name }

            + item("Change Log") { ChangeLog.open() }

            + item("Getting Started") { GettingStarted.open() }

            contentGroup("Guides", "guides/")

            + group(Strings.features) {
                + item("ZkArgPage") { ArgPage.open(ArgPage.Args(fourRandomInt()[0], "hello")) }
                + item("ZkCheckboxList") { CheckboxList.open() }
                + item("ZkConfirmDialog") { ConfirmDialog.open() }
                + item("ZkCrud") { BuiltinCrud.openAll() }
                + item("ZkForm") { FormFields.open() }
                + item("ZkTabContainer") { TabContainer.open() }
                + group("Table") {
                    + item("Generated") { GeneratedTable.open() }
                    + item("Fetched") { FetchedTable.open() }
                }
            }

            contentGroup("Contribute", "contribute/")
        }
    }

    private fun contentGroup(title: String, path: String) {
        + group(title) {
            contents.forEach {
                if (it.path.startsWith(path)) {
                    + item(it.name) { Content.open(it) }
                }
            }
        }
    }

    private fun hideMenu() {

    }

}



