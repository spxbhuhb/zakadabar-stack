/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema


/**
 * A text block with a stereotype for a content.
 */
@Serializable
class TextBlockBo(

    var stereotype : String,
    var value : String

) : BaseBo {

    override fun schema() = BoSchema {
        + ::stereotype max 100
        + ::value 
    }

}