/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.core.authorize.appRoles
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.SimpleRoleAuthorizer.Companion.LOGGED_IN
import zakadabar.core.authorize.SimpleRoleAuthorizer.Companion.PUBLIC
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.module.module
import zakadabar.core.data.StringPair

class TranslationBl : EntityBusinessLogicBase<TranslationBo>(
    boClass = TranslationBo::class
) {

    override val pa = TranslationExposedPa()

    private val localeBl by module<LocaleBl>()

    override val authorizer by provider {
        if (this is SimpleRoleAuthorizer) {
            allReads = LOGGED_IN
            allWrites = appRoles.securityOfficer
            query(TranslationsByLocale::class, PUBLIC)
        }
    }

    override val router = router {
        query(TranslationsByLocale::class, ::translationsByLocale)
    }

    @Suppress("UNUSED_PARAMETER") // used fro mapping
    private fun translationsByLocale(executor: Executor, query: TranslationsByLocale): List<StringPair> {
        val localeId = localeBl.byName(query.locale)?.id ?: return emptyList()
        return pa.translationsByLocale(localeId)
    }

}