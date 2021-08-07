/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.alarm.AlarmSupportProvider
import zakadabar.stack.alarm.LogAlarmSupportProvider
import zakadabar.stack.backend.audit.AuditorProvider
import zakadabar.stack.backend.audit.LogAuditorProvider
import zakadabar.stack.backend.ktor.KtorRouterProvider
import zakadabar.stack.backend.route.RouterProvider
import zakadabar.stack.backend.validate.SchemaValidatorProvider
import zakadabar.stack.backend.validate.ValidatorProvider

actual var routerProvider: RouterProvider = KtorRouterProvider()

actual var auditorProvider: AuditorProvider = LogAuditorProvider()

actual var validatorProvider: ValidatorProvider = SchemaValidatorProvider()

actual var alarmSupportProvider: AlarmSupportProvider = LogAlarmSupportProvider()