/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import kotlinx.browser.window
import org.intellij.markdown.MarkdownElementTypes
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.site.frontend.Routing
import zakadabar.site.frontend.components.SiteMarkdownImage
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.resources.SiteStrings
import zakadabar.stack.data.builtin.account.SessionDto
import zakadabar.stack.data.builtin.resources.StringsByLocale
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkExecutor
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

fun main() {

    io {

        ZkElement.addKClass = true

        val session = SessionDto.read(0L)

        with(ZkApplication) {

            executor = ZkExecutor(session.account, session.anonymous, session.roles)

            themes += SiteDarkTheme()
            themes += SiteLightTheme()

            theme = initTheme()

            val locale = session.account.locale ?: window.navigator.language

            strings = SiteStrings().merge(StringsByLocale(locale).execute())

            routing = Routing

            configureMarkdown()

            init()
        }

    }

}

/**
 * Overrides for markdown views according to our own content API.
 */
fun configureMarkdown() {
    MarkdownView.lib[MarkdownElementTypes.IMAGE] = { SiteMarkdownImage(this, it) }
}