/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.exception

/**
 * On the backend [DataConflict] is converted into HTTP response with status 409.
 *
 * On the frontend HTTP response with status 409 is converted into a [DataConflict].
 *
 * Form automatically handles this exception and informs the user that the operation
 * failed.
 */
class DataConflict(override val message: String) : Exception()