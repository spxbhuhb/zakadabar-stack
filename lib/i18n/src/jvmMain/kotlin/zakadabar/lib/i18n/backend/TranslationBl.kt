/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer.Companion.PUBLIC
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.builtin.misc.StringPair

class TranslationBl : EntityBusinessLogicBase<TranslationBo>(
    boClass = TranslationBo::class
) {

    override val pa = TranslationExposedPa()

    private val localeBl by module<LocaleBl>()

    override val authorizer by provider {
        if (this is SimpleRoleAuthorizer) {
            allReads = StackRoles.siteMember
            allWrites = StackRoles.siteAdmin
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