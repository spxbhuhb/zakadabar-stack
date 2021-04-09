/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.data.ContentEntry
import zakadabar.site.data.ContentQuery
import zakadabar.site.frontend.pages.misc.ChangeLog
import zakadabar.site.frontend.pages.misc.Content
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

    lateinit var contents: List<ContentEntry>

    override fun onCreate() {
        super.onCreate()

        io {
            contents = ContentQuery().execute().sortedBy { it.name }

            + title(Strings.applicationName, ::hideMenu) { Home.open() }

            + item("Change Log") { ChangeLog.open() }

            contentGroup("Guides", "guides/")

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



