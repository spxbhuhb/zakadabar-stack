/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.lib.accounts.data.AccountPrivateBo
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

@Serializable
class Mail(

    override var id : EntityId<Mail>,
    var status : MailStatus,
    var createdBy : EntityId<AccountPrivateBo>?,
    var createdAt : Instant?,
    var sentAt : Instant?,
    var sensitive : Boolean,
    var recipients : String,
    var subject : String

) : EntityBo<Mail> {

    companion object : EntityBoCompanion<Mail>("zk-mail")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::status default MailStatus.Preparation
        + ::createdBy
        + ::createdAt
        + ::sentAt
        + ::sensitive default false
        + ::recipients blank false
        + ::subject blank false max 200
    }

}