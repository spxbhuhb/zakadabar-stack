/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import kotlinx.browser.window
import zakadabar.site.frontend.Routing
import zakadabar.site.resources.SiteStrings
import zakadabar.stack.data.builtin.account.SessionDto
import zakadabar.stack.data.builtin.resources.StringsByLocale
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkExecutor
import zakadabar.stack.frontend.builtin.ZkBuiltinTheme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

fun main() {

    io {

        ZkElement.addKClass = true

        val session = SessionDto.read(0L)

        with(ZkApplication) {

            executor = ZkExecutor(session.account, session.anonymous, session.roles)

            theme = ZkBuiltinTheme()

            val locale = session.account.locale ?: window.navigator.language

            strings = SiteStrings().merge(StringsByLocale(locale).execute())

            routing = Routing

            init()
        }

    }

}