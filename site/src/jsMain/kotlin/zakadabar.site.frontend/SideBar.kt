/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.examples.frontend.form.FormFieldsGenerated
import zakadabar.lib.examples.frontend.form.SyntheticForm
import zakadabar.lib.examples.frontend.layout.TabContainer
import zakadabar.lib.examples.frontend.pages.ArgPage
import zakadabar.lib.examples.frontend.query.QueryPage
import zakadabar.lib.examples.frontend.table.FetchedTable
import zakadabar.lib.examples.frontend.table.GeneratedTable
import zakadabar.site.frontend.pages.*
import zakadabar.site.resources.strings
import zakadabar.stack.StackRoles
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.pages.account.accounts.Accounts
import zakadabar.stack.frontend.builtin.pages.account.roles.Roles
import zakadabar.stack.frontend.builtin.pages.resources.locales.Locales
import zakadabar.stack.frontend.builtin.pages.resources.settings.Settings
import zakadabar.stack.frontend.builtin.pages.resources.translations.Translations
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.text.MarkdownNav
import zakadabar.stack.util.fourRandomInt

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        io {
            val docSource = window.fetch("/api/content/guides/TOC.md").await().text().await()
            val changeLogSource = window.fetch("/api/content/changelog/TOC.md").await().text().await()

            + section(Welcome) {
                + item(GetStarted)
                + item(ShowCase)
                + item(GetHelp)
            }

            + section(strings.tools) {
                + item(BenderPage, text = strings.bender)
            }

            + group(DocumentationIntro, strings.documentation, section = true) {
                MarkdownNav().parse(docSource).forEach {
                    + it.doc()
                }
                + item(FAQ)
            }

            + section(strings.other) {
                + group(WhatsNew, text = strings.changeLog) {
                    MarkdownNav().parse(changeLogSource).forEach {
                        + it.changelog()
                    }
                }
                + item(Roadmap)
                + item(ServicesAndSupport)
                + item(LegalNotices)
                + item(Credits)
            }

            withOneOfRoles(StackRoles.securityOfficer, StackRoles.siteAdmin) {

                + section(strings.administration) {

                    + item(Settings)

                    withRole(StackRoles.siteAdmin) {
                        + item(strings.locales) { Locales.openAll() }
                        + item(strings.translations) { Translations.openAll() }
                    }

                    withRole(StackRoles.securityOfficer) {
                        + item(strings.accounts) { Accounts.openAll() }
                        + item(strings.roles) { Roles.openAll() }
                    }

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

    private fun MarkdownNav.MarkdownNavItem.changelog(): ZkElement {
        return if (children.isEmpty()) {
            item(
                Documentation,
                "changelog/" + if (url.startsWith("./")) url.substring(2) else url,
                label
            )
        } else {
            group(label) {
                children.forEach { + it.changelog() }
            }
        }
    }

    @Deprecated("should be in documentation")
    private fun examples() = group(strings.examples) {
        + group("Browser") {

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



