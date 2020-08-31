/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import kotlinx.serialization.Serializable
import zakadabar.stack.Stack
import zakadabar.stack.data.DtoWithRecordCompanion
import zakadabar.stack.extend.DtoWithRecordContract
import zakadabar.stack.util.PublicApi

@Serializable
data class EntityDto(
    override val id: Long,
    val acl: Long?,
    val status: EntityStatus,
    val parentId: Long?,
    val type: String,
    val name: String,
    val size: Long,
    val revision: Long,
    val modifiedBy: Long,
    val modifiedAt: Long
) : DtoWithRecordContract<EntityDto> {

    companion object : DtoWithRecordCompanion<EntityDto>() {

        fun new(parentId: Long?, type: String, name: String) = EntityDto(
            id = 0,
            acl = null,
            status = EntityStatus.Active,
            parentId = parentId,
            type = type,
            name = name,
            size = 0,
            revision = 1,
            modifiedAt = 0,
            modifiedBy = 0
        )

        /**
         * Get an URL for the given entity or for entities in general.
         */
        @PublicApi
        fun dtoUrl(entityId: Long? = null) = when (entityId) {
            null -> "/api/${Stack.shid}/entities"
            else -> "/api/${Stack.shid}/entities/$entityId"
        }

        /**
         * Get url for an entity id and a view.
         *
         * @param  entityId  Id of the entity to get view URL for.
         * @param  viewName  The view to get the URL for.
         */
        fun viewUrl(entityId: Long?, viewName: String? = null) = when {
            entityId == null -> "/api/${Stack.shid}/entities"
            viewName != null -> "/api/${Stack.shid}/entities/$entityId/$viewName"
            else -> "/api/${Stack.shid}/entities/$entityId"
        }

        /**
         * Get URL for an entity revision.
         *
         * @param  entityId  Id of the entity to get content URL for.
         * @param  revision  Revision number or null to get the last revision.
         */
        fun revisionUrl(entityId: Long, revision: Long? = null) = when (revision) {
            null -> "/api/${Stack.shid}/entities/$entityId/revisions"
            else -> "/api/${Stack.shid}/entities/$entityId/revisions/$revision"
        }

        /**
         * Get URL for resolving and entity path.
         *
         * @param  entityPath  Path to resolve.
         */
        @PublicApi
        fun resolveUrl(entityPath: String) = "/api/${Stack.shid}/resolve/${entityPath.trim('/')}"

    }

    fun requireId(id: Long): EntityDto {
        require(this.id == id)
        return this
    }

    fun requireType(type: String): EntityDto {
        require(this.type == type)
        return this
    }

    override fun comm() = comm
}


