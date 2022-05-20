/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.base

import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

var globals by cssStyleSheet(Globals())

@Suppress("unused", "ClassName")
class Globals : ZkCssStyleSheet() {

    val html by cssClass("html") {
        styles["scroll-behavior"] = "smooth"
    }

    val mp0 by cssRule("*, *::before, *::after") {
        margin = 0.px
        padding = 0.px
    }

    val a1 by cssRule("a, a:link, a:visited") {
        textDecoration = "none !important"
    }

    val a2 by cssRule("a.link, .link, a.link:link, .link:link, a.link:visited, .link:visited") {
        color = "${Colors.dark.main} !important"
        transition = "color 150ms ease-in !important"
    }

    val a3 by cssRule("a.link:hover, .link:hover, a.link:focus, .link:focus") {
        color = "${Colors.info.main} !important"
    }

}