/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend.business

import com.charleskorn.kaml.Yaml
import org.slf4j.LoggerFactory
import zakadabar.site.cookbook.GetContent
import zakadabar.site.cookbook.Recipe
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.data.builtin.settings.ContentBackendSettings
import zakadabar.stack.data.entity.EntityId
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

            if (path.fileName.toString() != "recipe.yaml") return@forEach

            val recipe = try {
                Yaml.default.decodeFromString(Recipe.serializer(), Files.readAllBytes(path).decodeToString())
            } catch (ex: Exception) {
                logger.error("error while loading $path", ex)
                return@forEach
            }

            recipe.id = EntityId(recipe.title)

            val contentPath = path.resolveSibling("recipe.md")

            val content = try {
                Files.readAllBytes(contentPath).decodeToString()
            } catch (ex: Exception) {
                logger.error("error while loading $contentPath", ex)
                return@forEach
            }

            if (recipes.containsKey(recipe.id)) {
                logger.warn("duplicated recipe title: ${recipe.title}")
                return@forEach
            }

            recipes[recipe.id] = recipe to content
        }

    }

    override fun list(executor: Executor): List<Recipe> {
        return recipes.values.map { it.first }
    }

    open fun getContent(executor: Executor, query: GetContent): StringValue =
        recipes[query.recipeId]?.let { StringValue(it.second) } ?: throw NoSuchElementException()

}