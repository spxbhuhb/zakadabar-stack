/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.data

import kotlinx.datetime.Instant

class JobFailException(
    message : String,
    val failData : String,
    val retryAt : Instant?
) : Exception(message)