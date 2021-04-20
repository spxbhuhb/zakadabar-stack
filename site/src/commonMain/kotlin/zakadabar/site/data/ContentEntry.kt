/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:UseSerializers(
    InstantAsStringSerializer::class
)

package zakadabar.site.data

import kotlinx.datetime.Instant
import kotlinx.serialization.*
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.util.InstantAsStringSerializer

@Serializable
class ContentEntry(
    val name: String,
    val path: String,
    val lastModified: Instant
) : DtoBase