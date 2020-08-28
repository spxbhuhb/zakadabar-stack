/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.header

import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.util.PublicApi

@PublicApi
open class Header(
    title: String = "",
    val icon: SimpleElement? = null,
    @PublicApi val titleElement: SimpleElement = SimpleText(title),
    private val tools: List<SimpleElement> = emptyList()
) : ComplexElement() {

    val toolElement = ComplexElement()

    override fun init(): ComplexElement {
        super.init()

        element.classList.add(headerClasses.header)

        this += icon?.withOptionalClass(headerClasses.headerIcon)
        this += titleElement.withClass(headerClasses.text)
        this += toolElement.withClass(headerClasses.extensions)

        toolElement += tools

        return this
    }

}