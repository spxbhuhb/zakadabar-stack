/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.browser

import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.ZkApplication
import zakadabar.core.resource.ZkStringStore
import zakadabar.core.text.TranslationProvider
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.lib.i18n.resource.i18nStrings

fun install(routing: ZkAppRouting) {
    with(routing) {
        + LocaleCrud()
        + TranslationCrud()
    }
}

fun install(application : ZkApplication) {
    application.services += TranslationProviderImpl()
    application.stringStores += i18nStrings
}

class TranslationProviderImpl : TranslationProvider {

    override suspend fun <T : ZkStringStore> translate(store: T, locale: String): T {
        store.merge(TranslationsByLocale(locale).execute())
        return store
    }

}