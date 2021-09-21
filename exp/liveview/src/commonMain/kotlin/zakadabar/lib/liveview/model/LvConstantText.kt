/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.model

import kotlinx.serialization.Serializable

@Serializable
class LvConstantText(
    override var x : Double,
    override var y : Double,
    var text : String
) : LiveViewElement("constant-text")