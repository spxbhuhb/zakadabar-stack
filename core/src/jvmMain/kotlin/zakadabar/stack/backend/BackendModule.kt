/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend

interface BackendModule {

    /**
     * A function that is called when the module is loaded.
     */
    fun init() = Unit

    /**
     * A function that is called when the module is unloaded.
     */
    fun cleanup() = Unit

}