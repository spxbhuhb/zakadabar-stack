/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.cookbook

import kotlinx.serialization.Serializable
import zakadabar.cookbook.Recipe
import zakadabar.core.data.StringValue
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.schema.BoSchema

@Serializable
class GetContent(
    var recipeId : EntityId<Recipe>
) : QueryBo<StringValue> {

    override suspend fun execute() = comm.query(this, serializer(), StringValue.serializer())

    companion object : QueryBoCompanion(Recipe.boNamespace)

    override fun schema() = BoSchema {
        + ::recipeId
    }
}
