/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.lib.accounts.frontend.SessionManager
import zakadabar.site.frontend.Routing
import zakadabar.site.frontend.resources.GreenBlueTheme
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.resources.strings
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.resources.initTheme
import zakadabar.stack.frontend.util.io

fun main() {

    application = ZkApplication()

    io {

        with(application) {

            initSession(SessionManager())

            initTheme(SiteDarkTheme(), SiteLightTheme(), GreenBlueTheme())

            initLocale(strings)

            initRouting(Routing())

            run()

        }

    }

}