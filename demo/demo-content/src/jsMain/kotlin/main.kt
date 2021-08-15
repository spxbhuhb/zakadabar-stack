/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.lib.content.frontend.browser.textBlockStereotypes
import zakadabar.lib.demo.frontend.Routing
import zakadabar.lib.demo.resources.strings
import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.browser.application.application
import zakadabar.core.browser.theme.ZkGreenBlueTheme
import zakadabar.core.resource.initTheme
import zakadabar.core.browser.util.io

fun main() {

    application = ZkApplication()
    
    zakadabar.lib.accounts.frontend.install(application)
    zakadabar.lib.i18n.frontend.install(application)
    zakadabar.lib.content.frontend.browser.install(application)

    textBlockStereotypes = mutableListOf("main","summary","motto","quote")

    io {

        with(application) {

            initSession()

            initTheme(ZkGreenBlueTheme())

            initLocale(strings)

            initRouting(Routing())

            run()

        }

    }

}