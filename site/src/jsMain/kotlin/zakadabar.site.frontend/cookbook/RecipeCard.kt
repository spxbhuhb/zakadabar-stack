/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.cookbook

import zakadabar.cookbook.Recipe
import zakadabar.site.frontend.resources.siteStyles
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.css.JustifyContent
import zakadabar.core.browser.util.marginBottom

class RecipeCard(
    val recipe: Recipe
) : ZkElement() {

    override fun onCreate() {
        + siteStyles.cookbookCard

        + row {
            + JustifyContent.spaceBetween
            + div(siteStyles.cookbookCardTitle) { + recipe.title }
            + div { + recipe.level }
        } marginBottom 20

        + row {
            + JustifyContent.spaceBetween
            + div { + recipe.tags.joinToString(", ") }
            + div { + recipe.targets.joinToString(", ") }
        }

        on("click") {
            application.changeNavState(Cookbook, recipe.title)
        }

    }
}