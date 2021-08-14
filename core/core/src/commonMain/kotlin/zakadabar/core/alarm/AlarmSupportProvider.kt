/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.alarm

import zakadabar.core.backend.business.BusinessLogicCommon

interface AlarmSupportProvider {

    fun businessLogicAlarmSupport(businessLogic : BusinessLogicCommon<*>) : AlarmSupport

}