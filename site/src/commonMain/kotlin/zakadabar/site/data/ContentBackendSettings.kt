/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema

@Serializable
class ContentBackendSettings(

    var root: String = "./var/content"

) : DtoBase {

    override fun schema() = DtoSchema {
        + ::root default root
    }

}