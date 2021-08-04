/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.cookbook

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId

@Serializable
class Recipe(
    override var id : EntityId<Recipe> = EntityId(), // needed for Yaml load
    var title: String,
    val level: String,
    val targets: List<String>,
    val tags: List<String>
) : EntityBo<Recipe> {

    companion object : EntityBoCompanion<Recipe>("recipe")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}