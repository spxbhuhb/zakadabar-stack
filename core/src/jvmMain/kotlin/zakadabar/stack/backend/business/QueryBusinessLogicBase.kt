/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.business

import zakadabar.stack.backend.RoutedModule
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.persistence.EmptyPersistenceApi
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EmptyEntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.util.PublicApi
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject

/**
 * Base class for standalone action (without entity) business logics.
 */
@PublicApi
abstract class QueryBusinessLogicBase<RQ : QueryBo<RS>, RS : Any>(
    queryBoClass: KClass<RQ>
) : QueryBusinessLogicCommon<RQ, RS>(queryBoClass), RoutedModule {

    override val pa = EmptyPersistenceApi<EmptyEntityBo>()

    override fun routerProvider() = routerProvider

    override fun auditorProvider() = auditorProvider

    abstract override val authorizer: Authorizer<EmptyEntityBo>

    override val namespace
        get() = (queryBoClass.companionObject !!.objectInstance as QueryBoCompanion).boNamespace

    override val router = router {
        query(queryBoClass, ::execute)
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

    override fun actionWrapper(executor: Executor, func: (Executor, BaseBo) -> BaseBo, bo: BaseBo) : BaseBo {
        throw NoSuchElementException("${this::class.simpleName} does not support actions")
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