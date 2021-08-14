/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Launch a coroutine from common code.
 */
@DelicateCoroutinesApi
actual fun fork(
    context: CoroutineContext,
    start: CoroutineStart,
    block: suspend CoroutineScope.() -> Unit
): Job =

    GlobalScope.launch(context, start, block)
