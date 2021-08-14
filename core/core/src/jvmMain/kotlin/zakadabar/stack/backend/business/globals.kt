/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.alarm.AlarmSupportProvider
import zakadabar.core.alarm.LogAlarmSupportProvider
import zakadabar.core.backend.audit.AuditorProvider
import zakadabar.core.backend.audit.LogAuditorProvider
import zakadabar.core.backend.ktor.KtorRouterProvider
import zakadabar.core.backend.route.RouterProvider
import zakadabar.core.backend.validate.SchemaValidatorProvider
import zakadabar.core.backend.validate.ValidatorProvider

actual var routerProvider: RouterProvider = KtorRouterProvider()

actual var auditorProvider: AuditorProvider = LogAuditorProvider()

actual var validatorProvider: ValidatorProvider = SchemaValidatorProvider()

actual var alarmSupportProvider: AlarmSupportProvider = LogAlarmSupportProvider()