/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.exception

import kotlinx.serialization.Serializable

/**
 * On the backend [Unauthorized] is converted into HTTP response with status 401.
 *
 * On the frontend HTTP response with status 403 is converted into a [Unauthorized].
 *
 * Form automatically handles this exception and informs the user that the operation
 * failed.
 */
class Unauthorized(
    val reason : String = "",
    val data : UnauthorizedData = UnauthorizedData()
) : Exception()

@Serializable
class UnauthorizedData(
    val locked : Boolean = false,
    val missingRole : Boolean = false
)