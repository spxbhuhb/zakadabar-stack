/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.util

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import zakadabar.core.route.RoutedModule
import zakadabar.core.server.ContentBackendSettings
import zakadabar.core.setting.setting
import java.io.File

/**
 * A simple backend that serves a directory. Uses `static` of Ktor.
 */
class ContentBackend(
    private val namespace: String
) : RoutedModule {

    private val settings by setting<ContentBackendSettings>(namespace)

    override fun onInstallStatic(route: Any) {
        route as Route
        with(route) {
            static("/api/$namespace") {
                staticRootFolder = File(settings.root)
                files(".")
            }
        }
    }
}