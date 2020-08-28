/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses

/**
 * This element simply hides itself, so it won't be shown. May be used
 * to replace elements easily in constructors.
 */
open class HiddenElement : SimpleElement() {

    init {
        element.className = coreClasses.hidden
    }

}