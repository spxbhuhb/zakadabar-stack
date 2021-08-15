/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.server.ktor

import io.ktor.sessions.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

/**
 * Session data class passed to Ktor.
 *
 * @property account Id of the account this session belongs to.
 */
@Serializable
data class StackSession(
    val account: EntityId<out BaseBo>,
    val anonymous: Boolean,
    val roleIds: List<EntityId<out BaseBo>>,
    val roleNames: List<String>,
)

object StackSessionSerializer : SessionSerializer<StackSession> {

    override fun deserialize(text: String) = Json.decodeFromString(StackSession.serializer(), text)

    override fun serialize(session: StackSession) = Json.encodeToString(StackSession.serializer(), session)

}