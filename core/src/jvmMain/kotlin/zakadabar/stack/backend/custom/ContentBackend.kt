/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.custom

import io.ktor.http.content.*
import io.ktor.routing.*
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.data.builtin.settings.ContentBackendSettings
import java.io.File

/**
 * A simple backend that serves a directory. Uses `static` of Ktor.
 */
class ContentBackend(
    private val namespace: String
) : BackendModule {

    private val settings by setting<ContentBackendSettings>(namespace)

    override fun onInstallStatic(route: Route) {
        with(route) {
            static("/api/$namespace") {
                staticRootFolder = File(settings.root)
                files(".")
            }
        }
    }
}