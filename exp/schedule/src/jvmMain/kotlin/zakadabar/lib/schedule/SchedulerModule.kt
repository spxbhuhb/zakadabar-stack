/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule

import zakadabar.lib.schedule.business.JobBl
import zakadabar.lib.schedule.business.SubscriptionBl
import zakadabar.core.authorize.appRoles
import zakadabar.core.module.CommonModule
import zakadabar.core.module.modules

class SchedulerModule : CommonModule {

    override fun onModuleLoad() {
        modules += JobBl(appRoles.securityOfficer, appRoles.securityOfficer, appRoles.securityOfficer)
        modules += SubscriptionBl()
    }

}