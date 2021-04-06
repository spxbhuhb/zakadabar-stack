/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.data

import kotlinx.serialization.*
import zakadabar.stack.data.DtoBase

@Serializable
class DocumentTreeEntry(
    val name: String,
    val path: String
) : DtoBase