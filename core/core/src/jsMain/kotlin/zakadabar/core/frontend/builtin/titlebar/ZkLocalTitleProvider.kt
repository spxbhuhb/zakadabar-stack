/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.titlebar

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.resources.localizedStrings

/**
 * Implemented by elements that are able to provide a local title.
 *
 * @property  titleText      Text content of the title. When set and titleElement is not set, an
 *                           unnamed element is created with [text] as content.
 * @property  titleElement   Element content of the title. When set [titleText] is ignored.
 */
interface ZkLocalTitleProvider {

    var addLocalTitle : Boolean
    var titleText: String?
    var titleElement: ZkAppTitle?

    fun buildLocalTitleBar(contextElements : List<ZkElement> = emptyList()) : ZkElement? {
        if (! addLocalTitle) return null

        titleElement?.let { return it }

        return ZkLocalTitleBar(titleText ?: localizedStrings.getNormalized(this::class.simpleName ?: ""), contextElements)
    }
}