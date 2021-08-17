/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.business

import zakadabar.core.alarm.AlarmSupportProvider
import zakadabar.core.alarm.LogAlarmSupportProvider
import zakadabar.core.audit.AuditorProvider
import zakadabar.core.audit.LogAuditorProvider
import zakadabar.core.log.LoggerProvider
import zakadabar.core.log.Slf4jLoggerProvider
import zakadabar.core.route.RouterProvider
import zakadabar.core.server.ktor.KtorRouterProvider
import zakadabar.core.validate.SchemaValidatorProvider
import zakadabar.core.validate.ValidatorProvider

actual var routerProvider: RouterProvider = KtorRouterProvider()

actual var auditorProvider: AuditorProvider = LogAuditorProvider()

actual var validatorProvider: ValidatorProvider = SchemaValidatorProvider()

actual var alarmSupportProvider: AlarmSupportProvider = LogAlarmSupportProvider()

actual var loggerProvider: LoggerProvider = Slf4jLoggerProvider()