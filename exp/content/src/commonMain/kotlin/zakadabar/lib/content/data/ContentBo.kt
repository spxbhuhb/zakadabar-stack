/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class ContentBo(

    override var id : EntityId<ContentBo>,
    var modifiedAt : Instant,
    var modifiedBy : EntityId<AccountPublicBo>,
    var status : EntityId<StatusBo>,
    var stereotype : EntityId<StereotypeBo>,
    var master: EntityId<ContentBo>?,
    var position: Int,
    var locale : EntityId<LocaleBo>?,
    var title : String,
    var localizedTitle : String,
    var summary : String,
    var textBlocks : List<TextBlockBo> = emptyList()

) : EntityBo<ContentBo> {

    companion object : EntityBoCompanion<ContentBo>("zkl-content-common")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::modifiedAt 
        + ::modifiedBy
        + ::locale
        + ::status
        + ::stereotype
        + ::master
        + ::position
        + ::title max 100
        + ::localizedTitle max 100
        + ::summary max 1000
        // FIXME + ::textBlocks
    }

}