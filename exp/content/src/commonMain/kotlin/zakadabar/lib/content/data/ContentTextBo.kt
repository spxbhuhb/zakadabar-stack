/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


/**
 * A text block with a stereotype for a content.
 */
@Serializable
class ContentTextBo(

    var stereotype : EntityId<ContentStereotypeBo>,
    var value : String

) : BaseBo {

    override fun schema() = BoSchema {
        + ::stereotype
        + ::value 
    }

}