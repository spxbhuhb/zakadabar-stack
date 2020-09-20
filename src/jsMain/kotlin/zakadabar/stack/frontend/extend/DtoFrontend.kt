/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import kotlinx.serialization.json.Json
import zakadabar.stack.comm.http.Comm
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.IconSource
import zakadabar.stack.frontend.comm.rest.FrontendComm
import zakadabar.stack.frontend.elements.ComplexElement
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

    val comm: Comm<T>? = FrontendComm(companion.type, companion.serializer())
    val type: String = companion.type
    val displayName: String = t(companion.type, namespace)
    val iconSource: IconSource? = null

    init {
        if (comm != null) companion.comm = comm
    }

    fun decodeQuery(queryName: String, queryData: String): Any {
        val queryDtoCompanion = companion.queries[queryName] ?: throw IllegalStateException("missing query: $queryName")
        return Json.decodeFromString(queryDtoCompanion.serializer(), queryData)
    }

    @PublicApi
    fun createView() = ComplexElement() build {
        + "create view for ${this::class.simpleName} is not implemented"
    }

    @PublicApi
    fun readView() = ComplexElement() build {
        + "read view for ${this::class.simpleName} is not implemented"
    }

    @PublicApi
    fun updateView() = ComplexElement() build {
        + "update view for ${this::class.simpleName} is not implemented"
    }

    @PublicApi
    fun deleteView() = ComplexElement() build {
        + "delete view for ${this::class.simpleName} is not implemented"
    }

    @PublicApi
    fun allView() = ComplexElement() build {
        + "all view for ${this::class.simpleName} is not implemented"
    }
}