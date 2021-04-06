/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.data.DocumentTreeQuery
import zakadabar.site.frontend.pages.misc.Home
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.util.io

object SideBar : ZkSideBar() {

    init {
        style {
            height = "100%"
            backgroundImage = """url("/menu_background.jpg")"""
            backgroundSize = "cover"
        }
    }

    override fun onCreate() {
        super.onCreate()

        + title(Strings.applicationName, ::hideMenu) { Home.open() }

        io {
            DocumentTreeQuery().execute().sortedBy { it.name }.forEach {
                + item(it.name) {
                    io {
                        val content = window.fetch("/api/content/${it.path}").await().text().await()
                        with(Home) {
                            clear()
                            + MarkdownView(content)
                        }
                    }
                }
            }
        }
    }


    private fun hideMenu() {

    }

}



