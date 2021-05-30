/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.frontend

import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.resources.ZkStringStore
import zakadabar.stack.text.TranslationProvider

fun install(routing: ZkAppRouting) {
    with(routing) {
        + Locales()
        + Translations()
    }
}

fun install(application : ZkApplication) {
    application.services += TranslationProviderImpl()
}

class TranslationProviderImpl : TranslationProvider {

    override suspend fun <T : ZkStringStore> translate(store: T, locale: String): T {
        store.merge(TranslationsByLocale(locale).execute())
        return store
    }

}