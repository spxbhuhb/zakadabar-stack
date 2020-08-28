/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.folder

import io.ktor.routing.*
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.backend.extend.entityRestApi
import zakadabar.stack.data.FolderDto
import zakadabar.stack.util.UUID

object Module : BackendModule() {

    override val uuid = UUID("82b9f17e-27eb-44e8-97a0-578c5404d087")

    override fun install(route: Route) = entityRestApi(route, Backend, FolderDto::class, FolderDto.type)

}