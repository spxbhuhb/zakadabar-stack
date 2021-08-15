/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import zakadabar.lib.content.data.FolderQuery
import zakadabar.lib.content.data.NavEntry
import zakadabar.lib.content.data.NavQuery
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.resource.css.px
import zakadabar.core.browser.util.io

class ContentNavTest : ZkPage() {

    override fun onResume() {
        super.onResume()
        clear()
        io {
            val locales = LocaleBo.all()
            val folders = FolderQuery().execute()

            + h2 { + "Folders" }

            + table {
                folders.forEach {
                    + tr {
                        + td { + "# ${it.id}" }
                        + td { + it.title }
                    }
                }
            }

            locales.forEach {
                + h2 { + "Nav for ${it.name}" }
                render(NavQuery(it.name, null).execute(), 0)
            }
        }
    }

    private fun render(entries: List<NavEntry>, indent: Int) {
        + div {
            buildPoint.style.paddingLeft = (indent * 20).px
            entries.forEach {
                + p { + it.title }
                if (it.folder) render(it.children, indent + 1)
            }
        }
    }


}