/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.business

import zakadabar.cookbook.Recipe
import zakadabar.cookbook.RecipeParser
import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.Executor
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.EntityId
import zakadabar.core.data.StringValue
import zakadabar.core.data.toStringValue
import zakadabar.core.persistence.EmptyPersistenceApi
import zakadabar.core.server.ContentBackendSettings
import zakadabar.core.setting.setting
import zakadabar.site.cookbook.GetContent
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Business Logic for Recipe.
 *
 * Generated with Bender at 2021-07-20T04:43:58.127Z.
 */
open class RecipeBl : EntityBusinessLogicBase<Recipe>(
    boClass = Recipe::class
) {

    open val settings by setting<ContentBackendSettings>("content")

    override val pa = EmptyPersistenceApi<Recipe>()

    override val authorizer: BusinessLogicAuthorizer<Recipe> by provider()

    override val router = router {
        query(GetContent::class, ::getContent)
    }

    open val recipes = mutableMapOf<EntityId<Recipe>, Pair<Recipe, Path>>()

    override fun onModuleStart() {
        super.onModuleStart()
        loadRecipes()
    }

    open fun loadRecipes() {

        Files.walk(Paths.get(settings.root).resolve("cookbook")).forEach { path ->
            try {

                if (path.fileName.toString() != "recipe.md") return@forEach

                val parser = RecipeParser(Files.readAllLines(path, StandardCharsets.UTF_8))
                parser.parse()

                val recipe = parser.recipe

                if (recipes.containsKey(parser.recipe.id)) {
                    logger.warn("duplicated recipe title: ${recipe.title}")
                    return@forEach
                }

                recipe.id = EntityId(path.toString().substringAfter("/cookbook/"))
                recipes[recipe.id] = recipe to path

            } catch (ex: Exception) {
                logger.error("error while loading $path", ex)
                return@forEach
            }

        }

    }

    override fun list(executor: Executor): List<Recipe> {
        return recipes.values.map { it.first }
    }

    open fun getContent(executor: Executor, query: GetContent): StringValue =
        recipes[query.recipeId]?.let {
            RecipeParser(Files.readAllLines(it.second, StandardCharsets.UTF_8))
                .parse()
                .content
                .toStringValue()
        } ?: throw NoSuchElementException()


}