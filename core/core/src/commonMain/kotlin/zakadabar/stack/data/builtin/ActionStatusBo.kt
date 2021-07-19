/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo

@Serializable
class ActionStatusBo(
    val success: Boolean = true,
    val reason: String? = null
) : BaseBo