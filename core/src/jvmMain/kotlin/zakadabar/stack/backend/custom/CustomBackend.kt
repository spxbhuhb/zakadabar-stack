/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.custom

import org.slf4j.LoggerFactory
import zakadabar.stack.backend.BackendModule

/**
 * Base class for custom backends.
 */
abstract class CustomBackend : BackendModule {

    protected open val logger = LoggerFactory.getLogger(this::class.simpleName) !!

}