/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.business

import zakadabar.core.route.RoutedModule
import zakadabar.core.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import kotlin.reflect.KClass

/**
 * Convenience base class for standalone action (without entity) business logics.
 */
@Deprecated("use BusinessLogicCommon instead")
abstract class ActionBusinessLogicCommon<RQ : ActionBo<RS>, RS : Any?>(
    open val actionBoClass: KClass<RQ>
) : BusinessLogicCommon<BaseBo>(), ActionBusinessLogicWrapper, RoutedModule {

    override val router = router {
        action(actionBoClass, ::execute)
    }

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

    abstract fun execute(executor: Executor, bo: RQ): RS

}