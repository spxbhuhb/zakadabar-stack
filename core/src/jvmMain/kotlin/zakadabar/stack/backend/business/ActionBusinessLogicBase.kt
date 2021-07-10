/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for standalone action (without entity) business logics.
 */
@PublicApi
abstract class ActionBusinessLogicBase<RQ : ActionBo<RS>, RS : BaseBo>(
    actionBoClass: KClass<RQ>
) : ActionBusinessLogicCommon<RQ, RS>(actionBoClass), RoutedModule {

    override val pa = EmptyPersistenceApi<EmptyEntityBo>()

    abstract override val authorizer: Authorizer<EmptyEntityBo>

    override fun routerProvider() = routerProvider

    override fun auditorProvider() = auditorProvider

    override val namespace
        get() = (actionBoClass.companionObject !!.objectInstance as ActionBoCompanion).boNamespace

    override val router = router {
        action(actionBoClass, ::execute)
    }

    override fun onInstallRoutes(route: Any) {
        router.installRoutes(route)
    }

    abstract override fun execute(executor: Executor, bo: RQ): RS

    override fun listWrapper(executor: Executor): List<EmptyEntityBo> {
        throw NotImplementedError("${this::class.simpleName} does not support CRUD operations")
    }

    override fun readWrapper(executor: Executor, entityId: EntityId<EmptyEntityBo>): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun createWrapper(executor: Executor, bo: EmptyEntityBo): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun updateWrapper(executor: Executor, bo: EmptyEntityBo): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun deleteWrapper(executor: Executor, entityId: EntityId<EmptyEntityBo>) {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun queryWrapper(executor: Executor, func: (Executor, BaseBo) -> Any, bo: BaseBo): Any {
        throw NoSuchElementException("${this::class.simpleName} does not support queries")
    }

    override fun create(executor: Executor, bo: EmptyEntityBo): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun read(executor: Executor, entityId: EntityId<EmptyEntityBo>): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun update(executor: Executor, bo: EmptyEntityBo): EmptyEntityBo {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun delete(executor: Executor, entityId: EntityId<EmptyEntityBo>) {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }

    override fun list(executor: Executor): List<EmptyEntityBo> {
        throw NoSuchElementException("${this::class.simpleName} does not support CRUD operations")
    }
}