/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.util

import zakadabar.stack.util.UUID

fun UUID.native() = java.util.UUID(msb, lsb)