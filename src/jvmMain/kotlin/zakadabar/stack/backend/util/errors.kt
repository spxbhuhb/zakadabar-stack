/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

/**
 * Base exception to indicate that the request is not correct due to
 * missing data access rights. Throwing this exception in a handler
 * will lead to 401 Unauthorized response.
 */
open class AuthorizationException(message: String, cause: Throwable? = null) : Exception(message, cause)