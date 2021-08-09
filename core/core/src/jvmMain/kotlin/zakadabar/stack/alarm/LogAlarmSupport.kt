/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.alarm

import org.slf4j.LoggerFactory
import zakadabar.stack.backend.business.BusinessLogicCommon

/**
 * Uses an sfl4j logger to write out the alarms as errors.
 */
class LogAlarmSupport(
    val businessLogic: BusinessLogicCommon<*>
) : AlarmSupport {

    val logger by lazy { LoggerFactory.getLogger(businessLogic.namespace) }

    override fun create(ex: Exception) {
        logger.error(ex.message, ex)
    }


}