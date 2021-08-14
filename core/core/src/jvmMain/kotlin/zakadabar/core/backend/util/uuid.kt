/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.util

import zakadabar.core.util.PublicApi
import zakadabar.core.util.UUID

@PublicApi
fun UUID.toJavaUuid() = java.util.UUID(msb, lsb)

@PublicApi
fun java.util.UUID.toStackUuid() = UUID(mostSignificantBits, leastSignificantBits)