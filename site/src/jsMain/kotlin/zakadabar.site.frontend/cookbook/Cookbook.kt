/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.cookbook

import zakadabar.cookbook.Recipe
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.site.cookbook.GetContent
import zakadabar.site.frontend.SiteMarkdownContext
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPathPage
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.resources.css.OverflowY
import zakadabar.stack.frontend.resources.css.em
import zakadabar.stack.frontend.resources.css.fr
import zakadabar.stack.frontend.resources.css.percent
import zakadabar.stack.frontend.util.io

object Cookbook : ZkPathPage(cssClass = zkPageStyles.fixed) {

    override fun onResume() {
        super.onResume()

        + zkLayoutStyles.grid1

        if (path.isEmpty()) {
            + Table()
            - firstOrNull<MarkdownView>()
        } else {
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

            + zkLayoutStyles.fixBorder
            + zkLayoutStyles.roundBorder

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
                matcher = { row, filter -> row.targets.firstOrNull { filter in it } != null}
            } size 10.em

            + custom {
                label = "Tags"
                render = { + it.tags.joinToString(", ") }
                matcher = { row, filter -> row.tags.firstOrNull { filter in it } != null}
            } size "max-content"
        }

        override fun onCreate() {
            super.onCreate()
            io {
                setData(Recipe.all())
            }
        }

        override fun getRowId(row: Recipe): String {
            return row.title
        }
        override fun onDblClick(id: String) {
            application.changeNavState(Cookbook, id)
        }

    }
}