/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

import kotlinx.browser.window
import kotlinx.coroutines.await

@PublicApi
suspend fun fetchText(url : String) : String = window.fetch(url).await().text().await()