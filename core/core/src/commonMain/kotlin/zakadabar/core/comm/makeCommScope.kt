/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.coroutines.CoroutineScope

/**
 * Function to create the comm coroutine scope.
 */
expect fun makeCommScope(): CoroutineScope
