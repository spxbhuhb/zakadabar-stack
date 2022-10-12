/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.business

import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.appRoles
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.data.StringPair
import zakadabar.core.module.module
import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.lib.i18n.data.TranslationsMap
import zakadabar.lib.i18n.persistence.LocaleExposedPa
import zakadabar.lib.i18n.persistence.TranslationExposedPa

class TranslationBl : EntityBusinessLogicBase<TranslationBo>(
    boClass = TranslationBo::class
) {

    override val pa = TranslationExposedPa()

    private val localePa = LocaleExposedPa()

    private val localeBl by module<LocaleBl>()

    override val authorizer by provider {
        if (this is SimpleRoleAuthorizer) {
            allReads = LOGGED_IN
            allWrites = appRoles.securityOfficer
            query(TranslationsByLocale::class, PUBLIC)
            query(TranslationsMap::class, PUBLIC)
        }
    }

    override val router = router {
        query(TranslationsByLocale::class, ::translationsByLocale)
        query(TranslationsMap::class, ::translationsMap)
    }

    @Suppress("UNUSED_PARAMETER") // used for mapping
    fun translationsByLocale(executor: Executor, query: TranslationsByLocale): List<StringPair> {
        val localeId = localeBl.byName(query.locale)?.id ?: return emptyList()
        return pa.translationsByLocale(localeId)
    }

    fun translationsMap(executor: Executor, bo: TranslationsMap): Map<String, Map<String, String>> {
        val allLocales = localePa.list().associateBy { it.id }
        val allTranslations = pa.list()
        val translationMap: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

        allTranslations.forEach { t ->
            allLocales[t.locale]?.let { l ->
                if (translationMap.containsKey(l.name)) {
                    translationMap[l.name]?.put(t.key, t.value)
                } else {
                    translationMap[l.name] = mutableMapOf(t.key to t.value)
                }
            }
        }

        return translationMap
    }

}