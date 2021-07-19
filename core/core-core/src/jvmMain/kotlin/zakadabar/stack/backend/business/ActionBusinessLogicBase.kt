/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for standalone action (without entity) business logics.
 */
@PublicApi
abstract class ActionBusinessLogicBase<RQ : ActionBo<RS>, RS : Any>(
    actionBoClass: KClass<RQ>
) : ActionBusinessLogicCommon<RQ, RS>(actionBoClass), RoutedModule {

    override val namespace
        get() = (actionBoClass.companionObject !!.objectInstance as ActionBoCompanion).boNamespace

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

}