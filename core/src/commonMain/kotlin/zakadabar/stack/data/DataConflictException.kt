/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

/**
 * On the backend [DataConflictException] is converted into HTTP response with status 409.
 *
 * On the frontend HTTP response with status 409 is converted into a [DataConflictException].
 *
 * Form automatically handles this exception and informs the user that the operation
 * failed.
 */
class DataConflictException(override val message: String) : Exception()