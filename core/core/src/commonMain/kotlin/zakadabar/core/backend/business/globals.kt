/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.alarm.AlarmSupportProvider
import zakadabar.core.backend.audit.AuditorProvider
import zakadabar.core.backend.route.RouterProvider
import zakadabar.core.backend.validate.ValidatorProvider

expect var routerProvider: RouterProvider

expect var auditorProvider: AuditorProvider

expect var validatorProvider: ValidatorProvider

expect var alarmSupportProvider: AlarmSupportProvider
