/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.application

import zakadabar.core.module.CommonModule

interface ZkSessionManager : CommonModule {
    suspend fun init()
    suspend fun renew()
}