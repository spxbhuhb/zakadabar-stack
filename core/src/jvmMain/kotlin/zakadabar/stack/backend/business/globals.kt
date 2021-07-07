/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.audit.AuditorProvider
import zakadabar.stack.backend.audit.LogAuditorProvider
import zakadabar.stack.backend.ktor.KtorRouterProvider
import zakadabar.stack.backend.route.RouterProvider

var routerProvider: RouterProvider = KtorRouterProvider()

val auditorProvider: AuditorProvider = LogAuditorProvider()