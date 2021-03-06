/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.audit

import zakadabar.stack.backend.business.EntityBusinessLogicCommon
import zakadabar.stack.data.entity.EntityBo

class LogAuditorProvider : AuditorProvider {

    override fun <T : EntityBo<T>> businessLogicAuditor(businessLogic : EntityBusinessLogicCommon<T>) : Auditor<T> {
        return LogAuditor(businessLogic)
    }

}