/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

import zakadabar.core.module.CommonModule
import zakadabar.core.resource.ZkStringStore

interface TranslationProvider : CommonModule {
    suspend fun <T : ZkStringStore> translate(store : T, locale : String) : T
}