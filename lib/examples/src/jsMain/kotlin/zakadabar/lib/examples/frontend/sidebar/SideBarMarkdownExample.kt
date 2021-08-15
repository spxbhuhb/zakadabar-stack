/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.sidebar

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.HTMLElement
import zakadabar.lib.markdown.frontend.markdownStyles
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.tabcontainer.ZkTabContainer
import zakadabar.core.browser.tabcontainer.zkTabContainerStyles
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.resource.css.px
import zakadabar.core.browser.util.io
import zakadabar.core.resource.localizedStrings
import zakadabar.core.text.MarkdownNav

class ExampleMarkdownSideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        io {
            val source = window.fetch("/api/content/guides/TOC.md").await().text().await()

            MarkdownNav().parse(source).forEach {
                + it.doc()
            }
        }
    }

    private fun MarkdownNav.MarkdownNavItem.doc(): ZkElement {
        return if (children.isEmpty()) {
            item(
                ExampleMarkdownSideBarTarget,
                "guides/" + if (url.startsWith("./")) url.substring(2) else url,
                label
            )
        } else {
            group(label) {
                children.forEach { + it.doc() }
            }
        }
    }

}

object ExampleMarkdownSideBarTarget : ZkPathPage() {
    override fun onCreate() {
        super.onCreate()
        + column {
            + div { + "The path is: $path" }
            + div { + "Click on the button to go back." }
            + buttonPrimary(localizedStrings.back) { application.back() }
        }
    }
}


class SideBarMarkdownExample(
    element: HTMLElement
) : ZkElement(element) {

    val content = ZkElement(document.createElement("pre") as HTMLElement)

    override fun onCreate() {
        super.onCreate()

        + ZkTabContainer {

            height = 400.px

            + tab("Side Bar") {

                + zkTabContainerStyles.scrolledContent

                + zkLayoutStyles.fixBorder
                + markdownStyles.unMarkdown

                + ExampleMarkdownSideBar()

            }

            + tab("Markdown Source") {

                + zkTabContainerStyles.scrolledContent

                + zkLayoutStyles.fixBorder
                + zkLayoutStyles.fs80
                + zkLayoutStyles.pl1

                + content

                io {
                    content.innerText = window.fetch("/api/content/guides/TOC.md").await().text().await()
                }
            }
        } marginBottom 40
    }
}