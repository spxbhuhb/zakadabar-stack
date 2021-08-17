/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.server.ktor.plugin

import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import zakadabar.core.server.ktor.invoke
import zakadabar.core.server.ktor.plusAssign
import zakadabar.core.server.server
import zakadabar.core.module.CommonModule

class Module : CommonModule {

    override fun onModuleLoad() {
        server += ContentNegotiation

        server += ContentNegotiation {
            json(Json)
        }
    }

}