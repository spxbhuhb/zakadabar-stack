/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.browser

import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.module.modules
import zakadabar.core.resource.ZkStringStore
import zakadabar.core.text.TranslationProvider
import zakadabar.core.util.PublicApi
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.LocaleStatus
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.lib.i18n.resource.i18nStrings

@PublicApi
fun install(routing: ZkAppRouting) {
    with(routing) {
        + LocaleCrud()
        + TranslationCrud()
    }
}

@PublicApi
fun install(application : ZkApplication) {
    modules += TranslationProviderImpl()
    application.stringStores += i18nStrings
}

class TranslationProviderImpl : TranslationProvider {

    override suspend fun <T : ZkStringStore> translate(store: T, locale: String): T {
        store.merge(TranslationsByLocale(locale).execute())
        return store
    }

    override suspend fun getLocales(): List<Pair<String, String>> =
        LocaleBo.all().mapNotNull {
            if (it.status == LocaleStatus.Public) {
                it.name to it.description
            } else {
                null
            }
        }

}