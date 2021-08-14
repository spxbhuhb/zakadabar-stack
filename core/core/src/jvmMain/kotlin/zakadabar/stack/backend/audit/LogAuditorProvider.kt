/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.audit

import zakadabar.core.backend.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo

class LogAuditorProvider : AuditorProvider {

    override fun <T : BaseBo> businessLogicAuditor(businessLogic : BusinessLogicCommon<T>) : Auditor<T> {
        return LogAuditor(businessLogic)
    }

}