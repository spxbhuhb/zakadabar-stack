/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor.session

import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import zakadabar.stack.data.builtin.account.AccountPrivateBo
import zakadabar.stack.data.builtin.account.RoleBo
import zakadabar.stack.data.entity.EntityId

/**
 * Session data class passed to Ktor.
 *
 * @property account Id of the account this session belongs to.
 */
@Serializable
data class StackSession(
    val account: EntityId<AccountPrivateBo>,
    val roleIds: List<EntityId<RoleBo>>,
    val roleNames: List<String>
)

object StackSessionSerializer : SessionSerializer<StackSession> {

    override fun deserialize(text: String) = Json.decodeFromString(StackSession.serializer(), text)

    override fun serialize(session: StackSession) = Json.encodeToString(StackSession.serializer(), session)

}