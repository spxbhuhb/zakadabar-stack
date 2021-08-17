/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.css.fontfiles

import io.ktor.http.content.*
import io.ktor.routing.*
import zakadabar.core.route.RoutedModule

class FontFiles : RoutedModule {

    override fun onInstallStatic(route: Any) {
        with (route as Route) {
            static("fonts") {
                files("fonts")
            }
        }
    }

}