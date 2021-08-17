/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.audit

import zakadabar.core.authorize.Executor
import zakadabar.core.data.BaseBo
import zakadabar.core.data.ActionBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo

/**
 * Creates an audit record of the operation.
 */
interface BusinessLogicAuditor<T : BaseBo> {

    var includeData : Boolean

    fun auditList(executor: Executor)
    fun auditRead(executor: Executor, entityId: EntityId<T>)
    fun auditCreate(executor: Executor, entity: T)
    fun auditUpdate(executor: Executor, entity: T)
    fun auditDelete(executor: Executor, entityId: EntityId<T>)

    fun auditAction(executor: Executor, bo: ActionBo<*>)
    fun auditQuery(executor: Executor, bo: QueryBo<*>)

    fun auditCustom(executor: Executor, message: () -> String)

}