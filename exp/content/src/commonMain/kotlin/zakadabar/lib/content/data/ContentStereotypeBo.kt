/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


/**
 * Stereotype of a content or a part of a content. Example: "main text block",
 * "product description", "quote".
 *
 * You can configure and assign stereotypes to your content freely and then
 * select the content with the appropriate stereotype to show.
 *
 * You can put your stereotypes into a hierarchy, providing a parent to
 * a stereotype.
 *
 * @param   id       Id of the stereotype entity.
 * @param   parent   Id of the parent stereotype.
 * @param   key      Key of the stereotype. Used for translation.
 */
@Serializable
class ContentStereotypeBo(

    override var id : EntityId<ContentStereotypeBo>,
    var parent : EntityId<ContentStereotypeBo>?,
    var key : String

) : EntityBo<ContentStereotypeBo> {

    companion object : EntityBoCompanion<ContentStereotypeBo>("zkl-content-stereotype")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::parent
        + ::key blank false min 2 max 100
    }

}