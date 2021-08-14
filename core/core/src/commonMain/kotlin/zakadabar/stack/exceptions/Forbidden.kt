/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.exceptions

/**
 * On the backend [Forbidden] is converted into HTTP response with status 403.
 *
 * On the frontend HTTP response with status 403 is converted into a [Forbidden].
 *
 * Form automatically handles this exception and informs the user that the operation
 * failed.
 */
class Forbidden : Exception()