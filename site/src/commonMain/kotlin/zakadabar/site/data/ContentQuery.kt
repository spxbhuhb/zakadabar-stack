/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.site.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryDto
import zakadabar.stack.data.query.QueryDtoCompanion
import zakadabar.stack.util.PublicApi

@Serializable
@PublicApi
class ContentQuery : QueryDto<ContentEntry> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(ContentEntry.serializer()))

    companion object : QueryDtoCompanion<ContentEntry>("contents")

}