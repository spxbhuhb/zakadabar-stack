/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.cookbook

import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.site.cookbook.GetContent
import zakadabar.site.cookbook.Recipe
import zakadabar.site.frontend.SiteMarkdownContext
import zakadabar.site.frontend.resources.siteStyles
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.builtin.pages.ZkPathPage
import zakadabar.stack.frontend.util.io

object Cookbook : ZkPathPage() {

    val list = zke {
        io {
            Recipe.all().forEach {
                + RecipeCard(it)
            }
        }
    }

    override fun onCreate() {

    }

    override fun onResume() {
        super.onResume()
        if (path.isEmpty()) {
            + siteStyles.cookbook
            + list
            - firstOrNull<MarkdownView>()
        } else {
            - siteStyles.cookbook
            - list
            io {
                val md = GetContent(EntityId(path)).execute().value
                + MarkdownView(
                    sourceText = md,
                    context = SiteMarkdownContext("Cookbook", "")
                )
            }
        }
    }
}