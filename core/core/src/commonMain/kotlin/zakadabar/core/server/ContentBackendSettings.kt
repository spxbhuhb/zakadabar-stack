/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema

/**
 * @property  root       The directory to serve. All files are served from this directory.
 */
@Serializable
class ContentBackendSettings(

    var root: String

) : BaseBo {

    override fun schema() = BoSchema {
        + ::root
    }

}