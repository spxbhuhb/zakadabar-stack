/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.site.frontend.cookbook.Cookbook
import zakadabar.site.frontend.pages.*
import zakadabar.site.resources.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.text.MarkdownNav

class SideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        io {
            val docSource = window.fetch("/api/content/guides/TOC.md").await().text().await()
            val changeLogSource = window.fetch("/api/content/changelog/TOC.md").await().text().await()
            val upgradeSource = window.fetch("/api/content/upgrade/TOC.md").await().text().await()

            + section(strings.Welcome) {
                + item(Welcome)
                + item(GetStarted)
                + item(ShowCase)
                + item(GetHelp)
            }

            + section(strings.tools) {
                + item(BenderPage, text = strings.bender)
                + item<Cookbook>()
            }

            + section(strings.documentation) {
                MarkdownNav().parse(docSource).forEach {
                    + it.doc()
                }
                + item(FAQ)
            }

            + section(strings.other) {
                + item(ProjectStatus)
                + group(text = strings.changeLog) {
                    MarkdownNav().parse(changeLogSource).forEach {
                        + it.changelog()
                    }
                }
                + group(text = strings.upgrade) {
                    MarkdownNav().parse(upgradeSource).forEach {
                        + it.upgrade()
                    }
                }
                + item(Versioning)
                + item(Roadmap)
                + item(ServicesAndSupport)
                + item(LegalNotices)
                + item(Credits)
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

    private fun MarkdownNav.MarkdownNavItem.upgrade(): ZkElement {
        return if (children.isEmpty()) {
            item(
                Documentation,
                "upgrade/" + if (url.startsWith("./")) url.substring(2) else url,
                label
            )
        } else {
            group(label) {
                children.forEach { + it.upgrade() }
            }
        }
    }

}



