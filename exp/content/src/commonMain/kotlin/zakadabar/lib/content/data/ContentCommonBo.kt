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

/**
 * Business Object of ContentBo.
 * 
 * Generated with Bender at 2021-06-05T02:36:16.418Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class ContentCommonBo(

    override var id : EntityId<ContentCommonBo>,
    var modifiedAt : Instant,
    var modifiedBy : EntityId<AccountPublicBo>,
    var status : EntityId<ContentStatusBo>,
    var category : EntityId<ContentCategoryBo>,
    var master: EntityId<ContentCommonBo>?,
    var parent : EntityId<ContentCommonBo>?,
    var locale : EntityId<LocaleBo>,
    var title : String,
    var summary : String,
    var motto : String

) : EntityBo<ContentCommonBo> {

    companion object : EntityBoCompanion<ContentCommonBo>("zkl-content-common")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::modifiedAt 
        + ::modifiedBy
        + ::locale
        + ::title max 100
        + ::summary max 1000
        + ::status
        + ::category
        + ::master
        + ::parent
        + ::motto max 200
    }

}