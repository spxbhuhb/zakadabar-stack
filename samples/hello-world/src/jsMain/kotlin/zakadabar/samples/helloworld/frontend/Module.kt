/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.helloworld.frontend

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.extend.FrontendModule
import zakadabar.stack.util.UUID

/**
 * This is a module definition which is used to add things defined in
 * the module to the frontend. In this example the main function
 * defined in `main.kt` adds the modules to the frontend.
 */
object Module : FrontendModule() {

    // Each module have to have a UUID. You generate this once and then
    // forget the value. On Mac OS you can use the `uuidgen` command to
    // generate a value.

    override val uuid = UUID("023678b8-0939-4084-9228-202d41fb1872")

    /**
     * Initializes the module, basically just adds the objects we defined
     * in this module. You don't need to add everything here, just the
     * objects that are not added by other mechanisms.
     */
    override fun init() {

        // The translations we use in this module.
        // IMPORTANT adding the translations will change in the close future, using them will remain the same

        FrontendContext += uuid to translations
    }

}
