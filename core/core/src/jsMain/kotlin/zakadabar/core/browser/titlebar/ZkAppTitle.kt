/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar

import zakadabar.core.browser.ZkElement

/**
 * Element to shown in the application title bar as the title of the current page. You can extend
 * this class and render any kind of title you would like.
 *
 * @param  text   The textual title of the application/page. This will be shown in
 *                the browser title, history, etc.
 * @param  contextElements  Context elements of the title bar that are active on this specific page.
 */
open class ZkAppTitle(
    open val text: String,
    open val contextElements: List<ZkElement> = emptyList()
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()
        + text
    }

}