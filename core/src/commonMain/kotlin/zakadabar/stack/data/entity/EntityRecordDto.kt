/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:UseSerializers(InstantAsStringSerializer::class)

package zakadabar.stack.data.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.Stack
import zakadabar.stack.data.query.Queries
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.util.InstantAsStringSerializer
import zakadabar.stack.util.PublicApi

@Serializable
data class EntityRecordDto(
    override val id: Long,
    val acl: Long?,
    val status: EntityStatus,
    val parentId: Long?,
    val entityType: String,
    val name: String,
    val size: Long,
    val revision: Long,
    val modifiedBy: Long,
    val modifiedAt: Instant
) : RecordDto<EntityRecordDto> {

    companion object : RecordDtoCompanion<EntityRecordDto>() {

        override val recordType = "${Stack.shid}/entity"

        override val queries = Queries.build {
            + ChildrenQuery.Companion
        }

        fun new(parentId: Long?, entityType: String, name: String) = EntityRecordDto(
            id = 0,
            acl = null,
            status = EntityStatus.Active,
            parentId = parentId,
            entityType = entityType,
            name = name,
            size = 0,
            revision = 1,
            modifiedAt = Clock.System.now(),
            modifiedBy = 0
        )

        /**
         * Get an URL for the given entity or for entities in general.
         */
        @PublicApi
        fun dtoUrl(entityId: Long? = null) = when (entityId) {
            null -> "/api/$recordType"
            else -> "/api/$recordType/$entityId"
        }

        /**
         * Get an URL for the list of children of the given entity (or root).
         */
        @PublicApi
        fun childrenUrl(entityId: Long? = null) = when (entityId) {
            null -> "/api/$recordType"
            else -> "/api/$recordType?parent=$entityId"
        }

        /**
         * Get url for an entity id and a view.
         *
         * @param  entityId  Id of the entity to get view URL for.
         * @param  viewName  The view to get the URL for.
         */
        fun viewUrl(entityId: Long?, viewName: String? = null) = when {
            entityId == null -> "/api/$recordType"
            viewName != null -> "/api/$recordType/$entityId/$viewName"
            else -> "/api/$recordType/$entityId"
        }

        /**
         * Get URL for an entity revision.
         *
         * @param  entityId  Id of the entity to get content URL for.
         * @param  revision  Revision number or null to get the last revision.
         */
        fun revisionUrl(entityId: Long, revision: Long? = null) = when (revision) {
            null -> "/api/$recordType/$entityId/revisions"
            else -> "/api/$recordType/$entityId/revisions/$revision"
        }

        /**
         * Get URL for resolving and entity path.
         *
         * @param  entityPath  Path to resolve.
         */
        @PublicApi
        fun resolveUrl(entityPath: String) = "/api/$recordType/resolve/${entityPath.trim('/')}"
    }

    fun requireId(id: Long): EntityRecordDto {
        require(this.id == id)
        return this
    }

    fun requireType(type: String): EntityRecordDto {
        require(this.entityType == type)
        return this
    }

    @PublicApi
    fun dtoUrl() = Companion.dtoUrl(this.id)

    @PublicApi
    fun viewUrl(viewName: String? = null) = Companion.viewUrl(this.id, viewName)

    @PublicApi
    fun revisionUrl(revision: Long? = null) = Companion.revisionUrl(this.id, revision)

    @PublicApi
    fun childrenUrl() = Companion.childrenUrl(this.id)

    fun prettyPrint(): String {
        return "# ${id.toString().padEnd(6)}  ${name.padEnd(40)}  size: ${size.toString().padStart(8)}  type: ${entityType.padEnd(20)} rev: ${revision.toString().padEnd(6)}  $modifiedAt"
    }

    override fun comm() = comm

    override fun getRecordType() = recordType

}


