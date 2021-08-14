/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.custom

import io.ktor.http.content.*
import io.ktor.routing.*
import zakadabar.core.backend.RoutedModule
import zakadabar.core.backend.setting.setting
import zakadabar.core.data.builtin.settings.ContentBackendSettings
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