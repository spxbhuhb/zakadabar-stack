/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.errors

import org.w3c.fetch.Response

/**
 * Throw on client side when there is a communication error.
 */
class FetchError(
    val response: Response
) : Throwable()