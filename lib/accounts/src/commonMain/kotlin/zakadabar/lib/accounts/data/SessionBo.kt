/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.builtin.misc.ServerDescriptionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId

@Serializable
class SessionBo(

    override var id: EntityId<SessionBo>,
    val account: AccountPublicBo,
    val anonymous: Boolean,
    val roles: List<String>,
    val serverDescription: ServerDescriptionBo

) : EntityBo<SessionBo> {

    companion object : EntityBoCompanion<SessionBo>("session")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}