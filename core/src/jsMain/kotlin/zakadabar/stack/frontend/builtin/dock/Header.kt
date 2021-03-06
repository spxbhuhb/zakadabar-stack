/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.dock.HeaderClasses.Companion.headerClasses
import zakadabar.stack.util.PublicApi

@PublicApi
open class Header(
    title: String = "",
    val icon: ZkElement? = null,
    @PublicApi val titleElement: ZkElement = ZkElement().also { it.build { + title } },
    private val tools: List<ZkElement> = emptyList()
) : ZkElement() {

    val toolElement = ZkElement()

    override fun onCreate() {
        element.classList.add(headerClasses.header)

        this += icon?.withOptCss(headerClasses.headerIcon)
        this += titleElement withCss headerClasses.text
        this += toolElement.withCss(headerClasses.extensions)

        toolElement += tools
    }

}