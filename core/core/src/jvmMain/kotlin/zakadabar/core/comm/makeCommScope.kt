/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

actual fun makeCommScope() = CoroutineScope(Dispatchers.IO)
