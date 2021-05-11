/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import kotlinx.browser.window
import zakadabar.site.frontend.Routing
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.resources.SiteStrings
import zakadabar.stack.data.builtin.resources.TranslationsByLocale
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

fun main() {

    io {

        ZkElement.addKClass = true

        with(ZkApplication) {

            sessionManager.init()

            themes += SiteDarkTheme()
            themes += SiteLightTheme()

            theme = initTheme()

            val locale = executor.account.locale ?: window.navigator.language

            strings = SiteStrings().merge(TranslationsByLocale(locale).execute())

            routing = Routing

            init()

        }

    }

}