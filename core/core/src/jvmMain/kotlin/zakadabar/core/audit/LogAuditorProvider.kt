/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.audit

import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo

class LogAuditorProvider : AuditorProvider {

    override fun <T : BaseBo> businessLogicAuditor(businessLogic : BusinessLogicCommon<T>) : BusinessLogicAuditor<T> {
        return LogAuditor(businessLogic)
    }

}