/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.audit

import zakadabar.stack.backend.business.EntityBusinessLogicCommon
import zakadabar.stack.data.entity.EntityBo

interface AuditorProvider {

    fun <T : EntityBo<T>> businessLogicAuditor(businessLogic : EntityBusinessLogicCommon<T>) : Auditor<T>

}