/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.util.Unique

/**
 * Base class for frontend modules.
 */
abstract class FrontendModule : Unique {

    /**
     * An initialization function that is called when the module is loaded.
     */
    open fun init() = Unit

}