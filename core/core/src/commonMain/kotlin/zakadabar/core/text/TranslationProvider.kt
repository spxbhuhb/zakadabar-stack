/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

import zakadabar.core.module.CommonModule
import zakadabar.core.resource.ZkStringStore

interface TranslationProvider : CommonModule {
    suspend fun <T : ZkStringStore> translate(store : T, locale : String) : T

    /**
     * List the locales known by the system. First element of the list is the
     * locale name, second is the description. Description should be shown
     * to the user, name should be set in the account.
     */
    suspend fun getLocales() : List<Pair<String,String>>
}