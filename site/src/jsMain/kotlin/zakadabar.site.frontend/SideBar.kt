/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import hu.simplexion.rf.leltar.frontend.pages.roles.Roles
import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.examples.frontend.crud.BuiltinCrud
import zakadabar.lib.examples.frontend.form.FormFieldsGenerated
import zakadabar.lib.examples.frontend.form.SyntheticForm
import zakadabar.lib.examples.frontend.layout.TabContainer
import zakadabar.lib.examples.frontend.pages.ArgPage
import zakadabar.lib.examples.frontend.query.QueryPage
import zakadabar.lib.examples.frontend.table.FetchedTable
import zakadabar.lib.examples.frontend.table.GeneratedTable
import zakadabar.site.data.ContentEntry
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
import zakadabar.stack.text.MarkdownNav
import zakadabar.stack.util.fourRandomInt

object SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + item(Welcome)
        + item(WhatsNew)
        + item(ShowCase)
        + item(Roadmap)
        + item(GetStarted)
        + group(GetHelp) {
            + item(FAQ)
        }

        io {
            val source = window.fetch("/content/guides/TOC.md").await().text().await()
            + group(DocumentationIntro, "Documentation") {
                MarkdownNav().parse(source).forEach {
                    + it.doc()
                }
            }
        }

        + examples()

        //contentGroup("ChangeLog", "changelog/", true)

        withOneOfRoles(StackRoles.securityOfficer, StackRoles.siteAdmin) {

            + group(Strings.administration) {

                + item(Settings)

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

    private fun MarkdownNav.MarkdownNavItem.doc(): ZkElement {
        return if (children.isEmpty()) {
            item(
                Documentation,
                "guides/" + if (url.startsWith("./")) url.substring(2) else url,
                label
            )
        } else {
            group(label) {
                children.forEach { + it.doc() }
            }
        }
    }

    private fun examples() = group(Strings.examples) {
        + group("Browser") {

            + item(BuiltinCrud)

            + group("Form") {
                + item(FormFieldsGenerated)
                + item(SyntheticForm)
            }


            + group("Pages") {
                + item("ArgPage") { ArgPage.open(ArgPage.Args(fourRandomInt()[0], "hello")) }
            }

            + item(QueryPage)
            + item(Settings)
            + item(TabContainer)

            + group("Table") {
                + item(GeneratedTable)
                + item(FetchedTable)
            }

        }
    }

}



