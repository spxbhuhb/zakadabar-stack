/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo

/**
 * A general BO to send/receive simple key and value pairs.
 */
@Serializable
class StringPair(
    val first : String,
    val second : String
) : BaseBo