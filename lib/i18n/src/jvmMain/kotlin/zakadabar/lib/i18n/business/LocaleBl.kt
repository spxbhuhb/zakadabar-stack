/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.business

import zakadabar.core.authorize.AppRolesBase
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.appRoles
import zakadabar.core.business.EntityBusinessLogicBase
import zakadabar.core.route.BusinessLogicRouter
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.lib.i18n.data.LocalesByStatus
import zakadabar.lib.i18n.data.TranslationsByLocale
import zakadabar.lib.i18n.persistence.LocaleExposedPa
import javax.management.Query

class LocaleBl : EntityBusinessLogicBase<LocaleBo>(
    boClass = LocaleBo::class
) {

    override val pa = LocaleExposedPa()

    override val authorizer by provider {
        if (this is SimpleRoleAuthorizer) {
            allReads = PUBLIC
            allWrites = appRoles.securityOfficer
            query(LocalesByStatus::class, PUBLIC)
        }

    }

    override val router = router {
        query(LocalesByStatus::class, ::localesByStatus)
    }

    fun byName(name: String) = pa.byName(name)

    fun localesByStatus(executor: Executor, bo: LocalesByStatus) = pa.byStatus(bo.status)


}