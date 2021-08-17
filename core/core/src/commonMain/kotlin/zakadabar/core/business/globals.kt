/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.business

import zakadabar.core.alarm.AlarmSupportProvider
import zakadabar.core.audit.AuditorProvider
import zakadabar.core.log.LoggerProvider
import zakadabar.core.route.RouterProvider
import zakadabar.core.validate.ValidatorProvider

expect var routerProvider: RouterProvider

expect var auditorProvider: AuditorProvider

expect var validatorProvider: ValidatorProvider

expect var alarmSupportProvider: AlarmSupportProvider

expect var loggerProvider: LoggerProvider
