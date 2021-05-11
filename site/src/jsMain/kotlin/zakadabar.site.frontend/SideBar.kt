/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import zakadabar.lib.examples.frontend.crud.BuiltinCrud
import zakadabar.lib.examples.frontend.form.FormFieldsGenerated
import zakadabar.lib.examples.frontend.form.SyntheticForm
import zakadabar.lib.examples.frontend.layout.TabContainer
import zakadabar.lib.examples.frontend.pages.ArgPage
import zakadabar.lib.examples.frontend.query.QueryPage
import zakadabar.lib.examples.frontend.table.FetchedTable
import zakadabar.lib.examples.frontend.table.GeneratedTable
import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.site.frontend.pages.*
import zakadabar.site.resources.Strings
import zakadabar.stack.StackRoles
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.pages.resources.locales.Locales
import zakadabar.stack.frontend.builtin.pages.resources.settings.Settings
import zakadabar.stack.frontend.builtin.pages.resources.translations.Translations
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.util.fourRandomInt

object SideBar : ZkSideBar() {

    private lateinit var contents: List<ContentEntry>

    override fun onCreate() {
        super.onCreate()

        io {
            contents = ContentQuery().execute().sortedBy { it.name }

            + item(Strings.Welcome) { Welcome.open() }

            + item(Strings.showCase) { ShowCase.open() }

            + item(Strings.roadmap) { Roadmap.open() }

            + item(Strings.getStarted) { GetStarted.open() }

            + group(Strings.getHelp, { GetHelp.open() }) {
                + item(Strings.faq) { FAQ.open() }
            }

            contentGroup(Strings.documentation, "guides/")

            + examples()

            contentGroup("ChangeLog", "changelog/", true)

            withOneOfRoles(StackRoles.securityOfficer, StackRoles.siteAdmin) {

                + group(Strings.administration) {

                    + item(Strings.settings) { Settings.openAll() }

                    withRole(StackRoles.siteAdmin) {
                        + item(Strings.locales) { Locales.openAll() }
                        + item(Strings.translations) { Translations.openAll() }
                    }

                    withRole(StackRoles.securityOfficer) {
                        + item(Strings.accounts) { Accounts.openAll() }
                        + item(Strings.roles) { Roles.openAll() }
                    }

                }
            }

        }

    }

    class ContentTreeEntry(
        val name: String,
        val path: String,
        val relativePath: String,
        val groups: List<String> = relativePath.split("/").dropLast(1)
    )

    class ContentTreeGroup(
        val name: String,
        val subgroups: MutableList<ContentTreeGroup> = mutableListOf(),
        val entries: MutableList<ContentTreeEntry> = mutableListOf()
    ) {
        /**
         * Find a group based on group names.
         * Creates groups that does not exists.
         */
        fun find(groups: List<String>): ContentTreeGroup {
            if (groups.isEmpty()) return this

            val name = groups.first()

            var group = subgroups.firstOrNull { it.name == name }
            if (group != null) return if (groups.size == 1) group else group.find(groups.drop(1))

            group = ContentTreeGroup(name)
            subgroups += group

            return group.find(groups.drop(1))
        }

        fun toGroup(sortByDate: Boolean): ZkElement = group(name.capitalize()) {
            subgroups.sortedBy { it.name }.forEach { + it.toGroup(sortByDate) }
            entries.sortedBy { it.name }.forEach { + item(it.name) { ContentPages.open(it.path) } }
        }
    }

    private fun contentGroup(title: String, path: String, sortByDate: Boolean = false) {
        val root = ContentTreeGroup(title)

        contents.filter { it.path.startsWith(path) }.forEach {
            val entry = ContentTreeEntry(it.name, it.path, it.path.substring(path.length).trim('/'))
            val group = root.find(entry.groups)
            group.entries += entry
        }

        + root.toGroup(sortByDate)
    }

    private fun examples() = group(Strings.examples) {
        + group("Browser") {

            + item("Crud") { BuiltinCrud.openAll() }

            + group("Form") {
                + item("Fields") { FormFieldsGenerated.open() }
                + item("Synthetic") { SyntheticForm.open() }
            }


            + group("Pages") {
                + item("ArgPage") { ArgPage.open(ArgPage.Args(fourRandomInt()[0], "hello")) }
            }

            + item("Query") { QueryPage.open() }

            + item("Settings") { Settings.openAll() }

            + item("TabContainer") { TabContainer.open() }

            + group("Table") {
                + item("Generated") { GeneratedTable.open() }
                + item("Fetched") { FetchedTable.open() }
            }

        }
    }

}



