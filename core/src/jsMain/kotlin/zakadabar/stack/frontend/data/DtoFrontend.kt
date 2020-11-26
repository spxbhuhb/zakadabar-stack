/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.data

import kotlinx.serialization.json.Json
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.IconSource
import zakadabar.stack.frontend.comm.rest.FrontendComm
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID
import kotlin.reflect.KClass

/**
 * Defines frontend for a data record type. Instances of [DtoFrontend] are added
 * to the [FrontendContext][zakadabar.stack.frontend.FrontendContext] by module loaders.
 *
 * @param     namespace         The namespace for displayName that provides this entity type.
 * @property  companion         The companion object of the entity DTO.
 * @property  comm              The [FrontendComm] for the DTO (defaults to standard RestComm for the companion).
 * @property  type              Type of the entity (defaults to [companion] type).
 * @property  displayName       Type name to display to the user.
 * @property  iconSource        Type icon to display to the user.
 */
abstract class DtoFrontend<T : RecordDto<T>>(
    namespace: UUID,
    val companion: RecordDtoCompanion<T>
) {

    abstract val dtoClass: KClass<T>

    val comm: Comm<T>? = FrontendComm(companion.recordType, companion.serializer())
    val type: String = companion.recordType
    val displayName: String = t(companion.recordType, namespace)
    val iconSource: IconSource? = null

    init {
        if (comm != null) companion.comm = comm
    }

    fun decodeQuery(queryName: String, queryData: String): Any {
        val queryDtoCompanion = companion.queries[queryName] ?: throw IllegalStateException("missing query: $queryName")
        return Json.decodeFromString(queryDtoCompanion.serializer(), queryData)
    }

    @PublicApi
    open fun createView() = ZkElement() build {
        + "create view for ${this@DtoFrontend::class.simpleName} is not implemented"
    }

    @PublicApi
    open fun readView() = ZkElement() build {
        + "read view for ${this@DtoFrontend::class.simpleName} is not implemented"
    }

    @PublicApi
    open fun updateView() = ZkElement() build {
        + "update view for ${this@DtoFrontend::class.simpleName} is not implemented"
    }

    @PublicApi
    open fun deleteView() = ZkElement() build {
        + "delete view for ${this@DtoFrontend::class.simpleName} is not implemented"
    }

    @PublicApi
    open fun allView() = ZkElement() build {
        + "all view for ${this@DtoFrontend::class.simpleName} is not implemented"
    }
}