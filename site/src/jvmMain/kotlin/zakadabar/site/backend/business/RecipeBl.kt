/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.business

import org.slf4j.LoggerFactory
import zakadabar.cookbook.Recipe
import zakadabar.cookbook.RecipeParser
import zakadabar.site.cookbook.GetContent
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.data.builtin.settings.ContentBackendSettings
import zakadabar.stack.data.entity.EntityId
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Business Logic for Recipe.
 *
 * Generated with Bender at 2021-07-20T04:43:58.127Z.
 */
open class RecipeBl : EntityBusinessLogicBase<Recipe>(
    boClass = Recipe::class
) {

    open val logger = LoggerFactory.getLogger("RecipeBl") !!

    open val settings by setting<ContentBackendSettings>("content")

    override val pa = EmptyPersistenceApi<Recipe>()

    override val authorizer: Authorizer<Recipe> by provider()

    override val router = router {
        query(GetContent::class, ::getContent)
    }

    open val recipes = mutableMapOf<EntityId<Recipe>, Pair<Recipe, String>>()

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

                recipes[recipe.id] = recipe to parser.content

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
        recipes[query.recipeId]?.let { StringValue(it.second) } ?: throw NoSuchElementException()


}