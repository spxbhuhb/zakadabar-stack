/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.site.data.ContentQuery
import zakadabar.site.frontend.resources.LandingStyles
import zakadabar.site.resources.Strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.pages.ZkPageStyles
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom

object GetStarted : ZkPage() {

    private val content = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div(ZkPageStyles.content) {

            + row(LandingStyles.buttons) {
                + ZkButton(Strings.normalPeople) { load("/start/NormalPeople.md") } css LandingStyles.button css LandingStyles.buttonBlue marginRight 20
                + ZkButton(Strings.tldr) { load("/start/TLDR.md") } css LandingStyles.button css LandingStyles.buttonGreen marginRight 20
                + ZkButton(Strings.jungleWarriors) { load("/start/JungleWarriors.md") } css LandingStyles.button css LandingStyles.buttonRed marginRight 20
            } marginBottom 30

            + content
        }
    }

    fun load(path: String) = io {
        content.clear()
        content += MarkdownView("/api/${ContentQuery.dtoNamespace}$path")
    }

}