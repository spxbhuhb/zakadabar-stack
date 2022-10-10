/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

/**
 * Get locales by status
 */
@Serializable
class LocalesByStatus(
    var status: LocaleStatus? // if null, query all statuses
) : QueryBo<List<LocaleBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(LocaleBo.serializer()))

    companion object : QueryBoCompanion(LocaleBo.boNamespace)

}