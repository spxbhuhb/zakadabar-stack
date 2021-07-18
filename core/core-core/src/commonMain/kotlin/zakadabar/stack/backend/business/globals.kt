/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.audit.AuditorProvider
import zakadabar.stack.backend.route.RouterProvider
import zakadabar.stack.backend.validate.ValidatorProvider

expect var routerProvider: RouterProvider

expect var auditorProvider: AuditorProvider

expect var validatorProvider: ValidatorProvider