/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.browser.application.application
import zakadabar.core.browser.util.io
import zakadabar.core.resource.initTheme
import zakadabar.lib.demo.frontend.DemoDefParams
import zakadabar.lib.demo.frontend.Routing
import zakadabar.lib.demo.resources.strings
import zakadabar.softui.browser.theme.SuiLightTheme
import zakadabar.core.browser.table.zkDefaultTableParameters

fun main() {

    application = ZkApplication()

    application.pendingModificationsEnabled = true
    
    zakadabar.lib.accounts.browser.install(application)
    zakadabar.lib.i18n.browser.install(application)
    zakadabar.softui.browser.install()

    zkDefaultTableParameters = DemoDefParams()

    io {

        with(application) {

            initSession()

            initTheme(SuiLightTheme())

            initLocale(strings)

            initRouting(Routing())

            run()

        }

    }

}