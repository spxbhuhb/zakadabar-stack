/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses

/**
 * An element to use in customizable UI classes when there is an optional
 * element subclasses may want to implement. For example an optional
 * header or an optional icon.
 *
 * This element simply hides itself, so it won't be shown if the subclasses
 * does not replace it with something else.
 */
open class OptionalElement : SimpleElement() {

    init {
        element.className = coreClasses.hidden
    }

}