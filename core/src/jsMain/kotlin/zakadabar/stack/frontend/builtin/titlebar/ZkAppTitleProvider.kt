/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement

/**
 * Implemented by elements that are able to set the application title.
 *
 * @property  setAppTitle    When true, [setAppTitleBar] sets the application title. This
 *                           typically happens in onResume. When false, [setAppTitleBar] is a no-op.
 * @property  titleText      Text content of the title. When set and titleElement is not set, an
 *                           unnamed element is created with [text] as content.
 * @property  titleElement   Element content of the title. When set [titleText] is ignored.
 */
interface ZkAppTitleProvider {

    var setAppTitle : Boolean
    var titleText: String?
    var titleElement: ZkAppTitle?

    fun setAppTitleBar(contextElements : List<ZkElement> = emptyList()) {
        if (! setAppTitle) return

        titleElement?.let {
            application.title = it
            return
        }

        application.title = ZkAppTitle(titleText ?: stringStore.getNormalized(this::class.simpleName ?: ""), contextElements)
    }
}