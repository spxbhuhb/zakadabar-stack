/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.server.ktor.plugin

import io.ktor.features.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import zakadabar.stack.backend.ktor.invoke
import zakadabar.stack.backend.ktor.plusAssign
import zakadabar.stack.backend.server
import zakadabar.stack.module.CommonModule

class Module : CommonModule {

    override fun onModuleLoad() {
        server += ContentNegotiation

        server += ContentNegotiation {
            json(Json)
        }
    }

}