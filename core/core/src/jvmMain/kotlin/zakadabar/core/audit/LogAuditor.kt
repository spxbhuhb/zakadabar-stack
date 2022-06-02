/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.audit

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory
import zakadabar.core.authorize.Executor
import zakadabar.core.data.*
import zakadabar.core.server.server
import kotlin.reflect.full.createType

/**
 * A simple auditor that writes the audit into the logger (on INFO channel)
 * passed in the constructor.
 *
 * @param  forInstance  The instance (typically a BL) that uses the auditor.
 *
 * @property  includeData  When true, content of the BOs is also written for create and update.
 */
open class LogAuditor<T : BaseBo>(
    forInstance : Any
) : BusinessLogicAuditor<T> {

    private val logger = LoggerFactory.getLogger(forInstance::class.simpleName ?: "anonymous object") !!

    override var includeData: Boolean = true

    override fun auditList(executor: Executor) {
        if (server.settings.logReads) {
            logger.info("${executor.accountId}: LIST")
        }
    }

    override fun auditRead(executor: Executor, entityId: EntityId<T>) {
        if (server.settings.logReads) {
            logger.info("${executor.accountId}: READ $entityId")
        }
    }

    override fun auditCreate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        if (entity is EntityBo<*>) {
            logger.info("${executor.accountId}: CREATE ${entity.id} // $text")
        } else {
            logger.info("${executor.accountId}: CREATE // $text")
        }
    }

    override fun auditUpdate(executor: Executor, entity: T) {
        val text = if (includeData) Json.encodeToString(serializer(entity::class.createType()), entity) else ""
        if (entity is EntityBo<*>) {
            logger.info("${executor.accountId}: UPDATE ${entity.id} // $text")
        } else {
            logger.info("${executor.accountId}: UPDATE // $text")
        }
    }

    override fun auditDelete(executor: Executor, entityId: EntityId<T>) {
        logger.info("${executor.accountId}: DELETE $entityId")
    }

    override fun auditCustom(executor: Executor, message: () -> String) {
        logger.info("${executor.accountId}: CUSTOM ${message()}")
    }

    override fun auditQuery(executor: Executor, bo: QueryBo<*>) {
        if (server.settings.logReads) {
            val text = if (includeData) Json.encodeToString(serializer(bo::class.createType()), bo) else ""
            logger.info("${executor.accountId}: QUERY ${bo::class.simpleName} // $text")
        }
    }

    override fun auditAction(executor: Executor, bo: ActionBo<*>) {
        val text = if (includeData) Json.encodeToString(serializer(bo::class.createType()), bo) else ""
        logger.info("${executor.accountId}: ACTION ${bo::class.simpleName} // $text")
    }

}