/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.log

import zakadabar.core.business.BusinessLogicCommon

interface LoggerProvider {

    fun businessLogicLogger(businessLogic : BusinessLogicCommon<*>) : Logger

}