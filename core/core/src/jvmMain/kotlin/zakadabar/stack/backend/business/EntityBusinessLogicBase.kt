/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.backend.RoutedModule
import zakadabar.core.data.entity.EntityBo
import zakadabar.core.data.entity.EntityBoCompanion
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

abstract class EntityBusinessLogicBase<T : EntityBo<T>>(
    boClass: KClass<T>
): EntityBusinessLogicCommon<T>(
    boClass
), RoutedModule {

    override val namespace: String
        get() = (boClass.companionObject !!.objectInstance as EntityBoCompanion<*>).boNamespace

}