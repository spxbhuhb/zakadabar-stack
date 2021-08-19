/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.cookbook.Cookbook
import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.browser.application.application
import zakadabar.core.browser.util.io
import zakadabar.core.module.modules
import zakadabar.core.resource.initTheme
import zakadabar.site.frontend.Routing
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteGreenBlueTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.resources.strings

fun main() {

    application = ZkApplication()

    modules += Cookbook()

    io {

        with(application) {

            initSession()

            initTheme(SiteDarkTheme(), SiteLightTheme(), SiteGreenBlueTheme())

            initLocale(strings, defaultLocale = "en")

            initRouting(Routing())

            run()

        }

    }

}