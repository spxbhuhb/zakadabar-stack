/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.settings

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

/**
 * @property  namespace  The namespace part of the URL this backend serves. For example:
 *                       in https://zakadabar.io/api/content/Welcome.md the namespace is "content".
 * @property  root       The directory to serve. All files are served from this directory.
 *
 */
@Serializable
class ContentBackendSettings(

    var namespace : String,
    var root: String

) : BaseBo {

    override fun schema() = BoSchema {
        + ::namespace blank false max 50
        + ::root
    }

}