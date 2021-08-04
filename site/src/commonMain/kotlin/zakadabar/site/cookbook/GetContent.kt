/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.cookbook

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.StringValue
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema

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
