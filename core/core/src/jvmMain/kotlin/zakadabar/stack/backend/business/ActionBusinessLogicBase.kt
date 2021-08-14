/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.backend.business

import zakadabar.core.data.action.ActionBo
import zakadabar.core.data.action.ActionBoCompanion
import zakadabar.core.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for standalone action (without entity) business logics.
 */
@PublicApi
@Deprecated("use BusinessLogicCommon instead")
abstract class ActionBusinessLogicBase<RQ : ActionBo<RS>, RS : Any?>(
    actionBoClass: KClass<RQ>
) : ActionBusinessLogicCommon<RQ, RS>(actionBoClass) {

    override val namespace
        get() = (actionBoClass.companionObject !!.objectInstance as ActionBoCompanion).boNamespace

}