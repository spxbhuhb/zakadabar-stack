/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.model

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo

@Serializable
sealed class LiveViewElement(
    var type : String,
) : BaseBo {
    abstract var x : Double
    abstract var y : Double
}