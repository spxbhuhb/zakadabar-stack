/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.audit

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.util.Executor
import kotlin.reflect.full.createType

/**
 * A simple auditor that writes the audit into the logger (on INFO channel)
 * passed in the constructor.
 *
 * @param  forInstance  The instance (typically a BL) that uses the auditor.
 *
 * @property  includeData  When true, content of the BOs is also written for create and update.
 */
open class LogAuditor<T : EntityBo<T>>(
    forInstance : Any
) : Auditor<T> {

    private val logger = LoggerFactory.getLogger(forInstance::class.simpleName ?: "anonymous object") !!

    override var includeData: Boolean = true

    override fun auditList(executor: Executor) {
        logger.info("${executor.accountId}: LIST")
    }

    override fun auditRead(executor: Executor, entityId: EntityId<T>) {
        logger.info("${executor.accountId}: READ $entityId")
    }

    override fun auditCreate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        logger.info("${executor.accountId}: CREATE ${entity.id} // $text")
    }

    override fun auditUpdate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        logger.info("${executor.accountId}: UPDATE ${entity.id} // $text")
    }

    override fun auditDelete(executor: Executor, entityId: EntityId<T>) {
        logger.info("${executor.accountId}: DELETE $entityId")
    }

    override fun auditCustom(executor: Executor, message: () -> String) {
        logger.info("${executor.accountId}: CUSTOM ${message()}")
    }

    override fun auditQuery(executor: Executor, bo: QueryBo<*>) {
        val text = if (includeData) Json.encodeToString(serializer(bo::class.createType()), bo) else ""
        logger.info("${executor.accountId}: QUERY ${bo::class.simpleName} // $text")
    }

    override fun auditAction(executor: Executor, bo: ActionBo<*>) {
        val text = if (includeData) Json.encodeToString(serializer(bo::class.createType()), bo) else ""
        logger.info("${executor.accountId}: ACTION ${bo::class.simpleName} // $text")
    }

}