/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for standalone action (without entity) business logics.
 */
@PublicApi
abstract class QueryBusinessLogicBase<RQ : QueryBo<RS>, RS : Any?>(
    queryBoClass: KClass<RQ>
) : QueryBusinessLogicCommon<RQ, RS>(queryBoClass), RoutedModule {

    override val namespace
        get() = (queryBoClass.companionObject !!.objectInstance as QueryBoCompanion).boNamespace

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

}