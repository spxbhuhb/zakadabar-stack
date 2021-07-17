/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

abstract class EntityBusinessLogicBase<T : EntityBo<T>>(
    boClass: KClass<T>
): EntityBusinessLogicCommon<T>(
    boClass
), RoutedModule {

    override val namespace: String
        get() = (boClass.companionObject !!.objectInstance as EntityBoCompanion<*>).boNamespace

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

}