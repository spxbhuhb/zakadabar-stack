/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.backend

import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.Forbidden
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.data.builtin.misc.StringPair
import zakadabar.stack.data.query.QueryBo

class TranslationBl : EntityBusinessLogicBase<TranslationBo>(
    boClass = TranslationBo::class
) {

    override val pa = TranslationExposedPa()

    private val localeBl by module<LocaleBl>()

    override val authorizer = object : SimpleRoleAuthorizer<TranslationBo>({
        allReads = StackRoles.siteMember
        allWrites = StackRoles.siteAdmin
    }) {
        // allow reading the translations for everyone
        // TODO think about restricting translations before login
        override fun authorizeQuery(executor: Executor, queryBo: QueryBo<*>) {
            if (queryBo is TranslationsByLocale) return
            throw Forbidden()
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