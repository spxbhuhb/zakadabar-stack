/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.site.frontend.Routing
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.resources.strings
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.initTheme
import zakadabar.stack.frontend.util.io

fun main() {

    ZkElement.addKClass = false
    ZkCssStyleSheet.shortNames = true

    application = ZkApplication()

    io {

        with(application) {

            initSession()

            initTheme(SiteDarkTheme(), SiteLightTheme())

            initLocale(strings)

            initRouting(Routing())

            run()

        }

    }

}