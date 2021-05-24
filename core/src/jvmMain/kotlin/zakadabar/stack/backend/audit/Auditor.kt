/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.audit

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.Executor

/**
 * Creates an audit record of the operation.
 */
interface Auditor<T : BaseBo> {

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