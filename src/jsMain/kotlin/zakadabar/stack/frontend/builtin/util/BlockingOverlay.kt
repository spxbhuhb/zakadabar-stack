/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.util.PublicApi

/**
 * An overlay that blocks the whole subject with a control element.
 */
@PublicApi
open class BlockingOverlay(
    val subject: SimpleElement,
    val control: SimpleElement
) : ComplexElement() {

    override fun init(): BlockingOverlay {
        TODO("not implemented tey")
    }
}