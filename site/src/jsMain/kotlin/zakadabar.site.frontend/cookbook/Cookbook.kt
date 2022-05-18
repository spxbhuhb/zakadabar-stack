/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.cookbook

import zakadabar.cookbook.Recipe
import zakadabar.core.browser.application.application
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.io
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.*
import zakadabar.lib.markdown.browser.MarkdownView
import zakadabar.site.cookbook.GetContent
import zakadabar.site.frontend.SiteMarkdownContext

var savedTable: Cookbook.Table? = null

object Cookbook : ZkPathPage() {

    override fun onResume() {
        super.onResume()

        + Position.relative

        if (path.isEmpty()) {
            + zkLayoutStyles.grid1

            if (savedTable == null) {
                + Table().also { savedTable = it }
            } else {
                + savedTable?.apply { redraw() }
            }

            - firstOrNull<MarkdownView>()
        } else {
            - zkLayoutStyles.grid1
            - firstOrNull<Table>()
            io {
                val md = GetContent(EntityId(path)).execute().value
                + MarkdownView(
                    sourceText = md,
                    context = SiteMarkdownContext("Cookbook", "")
                )
            }
        }
    }

    class Table : ZkTable<Recipe>() {

        override fun onConfigure() {

            + OverflowY.auto
            height = 100.percent

            search = true
            oneClick = true

            addLocalTitle = true
            titleText = "Recipes"

            + Recipe::title size 1.fr
            + Recipe::level size 10.em

            + custom {
                label = "Targets"
                render = { + it.targets.joinToString(", ") }
                matcher = { row, filter -> row.targets.firstOrNull { filter in it } != null }
            } size 10.em

            + custom {
                label = "Tags"
                render = { + it.tags.joinToString(", ") }
                matcher = { row, filter -> row.tags.firstOrNull { filter in it } != null }
            } size "max-content"
        }

        override fun onCreate() {
            super.onCreate()
            io {
                setData(Recipe.all())
            }
        }

        override fun getRowId(row: Recipe): String {
            return row.id.value
        }

        override fun onDblClick(id: String) {
            application.changeNavState(Cookbook, id)
        }

    }
}