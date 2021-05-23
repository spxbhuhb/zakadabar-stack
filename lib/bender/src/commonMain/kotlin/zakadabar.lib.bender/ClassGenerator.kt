/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import zakadabar.stack.data.schema.descriptor.BoDescriptor

open class ClassGenerator {

    lateinit var boDescriptor: BoDescriptor
    lateinit var generators: List<PropertyGenerator>

    open val packageName
        get() = boDescriptor.packageName
    open val boName
        get() = boDescriptor.className
    open val namespace
        get() = boDescriptor.boNamespace
    open val baseName
        get() = boName.withoutDto()

    open val browserCrudName
        get() = baseName + "Crud"
    open val browserTableName
        get() = baseName + "Table"
    open val browserFormName
        get() = baseName + "Form"

    open val exposedBackendName
        get() = baseName + "Table"
    open val exposedTableName
        get() = baseName + "Table"
    open val exposedDaoName
        get() = baseName + "Dao"


    // @formatter:off

    // -------------------------------------------------------------------------
    //  Common
    // -------------------------------------------------------------------------

fun commonGenerator() = """
package ${packageName}.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.entity.EntityDtoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.DtoSchema
${generators.map { it.commonImport() }.flatten().distinct().joinToString("\n")}

@Serializable
class ${boName}(

    override var id: EntityId<$boName>,
    ${generators.joinToString(",\n    ") { it.commonDeclaration() }}

) : EntityDto<$boName> {

    companion object : EntityDtoCompanion<$boName>("$namespace")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        ${generators.joinToString("\n        ") { it.commonSchema() }}
    }

}
""".trimIndent()

    // -------------------------------------------------------------------------
    //  Frontend
    // -------------------------------------------------------------------------

fun browserFrontendGenerator() = """
package ${packageName}.frontend.pages

import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.application.translate

import ${packageName}.data.$boName

${generators.map { it.browserImport() }.flatten().distinct().joinToString("\n")}

// -----------------------------------------------------------------------------
//  Crud
// -----------------------------------------------------------------------------

object $browserCrudName : ZkCrudTarget<$boName>() {
    init {
        companion = $boName.Companion
        dtoClass = $boName::class
        pageClass = $browserFormName::class
        tableClass = $browserTableName::class
    }
}

// -----------------------------------------------------------------------------
//  Form
// -----------------------------------------------------------------------------

class $browserFormName : ZkForm<$boName>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<$browserFormName>()) {
            + section {
                ${generators.joinToString("\n                ") { it.browserForm() }}
            }
        }
    }
}

// -----------------------------------------------------------------------------
//  Table
// -----------------------------------------------------------------------------

class $browserTableName : ZkTable<$boName>() {

    override fun onConfigure() {

        crud = $browserCrudName

        titleText = translate<$browserTableName>()

        add = true
        search = true
        export = true
        
        ${generators.joinToString("\n        ") { it.browserTable() }}
        
        + actions()
    }
}
""".trimIndent()

    // -------------------------------------------------------------------------
    //  Exposed Backend
    // -------------------------------------------------------------------------

fun exposedBackendGenerator() = """
package ${packageName}.backend.entity

import io.ktor.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor
import ${packageName}.data.$boName
${generators.map { it.exposedBackendImport() }.flatten().distinct().joinToString("\n")}

// -----------------------------------------------------------------------------
//  Entity Backend
// -----------------------------------------------------------------------------

object ${boName.withoutDto()}Backend : EntityBackend<$boName>() {

    override val dtoClass = $boName::class

    override fun onModuleLoad() {
        + $exposedTableName
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteMember) // TODO authorization for $

        $exposedTableName
            .selectAll()
            .map($exposedTableName::toDto)
            
    }

    override fun create(executor: Executor, dto: $boName) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName
            .new { fromDto(dto) }
            .toDto()
           
    }

    override fun read(executor: Executor, entityId: EntityId<$boName>) = transaction {

        authorize(true)

        $exposedDaoName[entityId].toDto()
        
    }

    override fun update(executor: Executor, dto: $boName) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName[dto.id]
            .fromDto(dto)
            .toDto()
            
    }

    override fun delete(executor: Executor, entityId: EntityId<$boName>) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName[entityId].delete()
     
    }

}

// -----------------------------------------------------------------------------
//  Exposed DAO
// -----------------------------------------------------------------------------

class $exposedDaoName(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<$exposedDaoName>($exposedTableName)

    ${generators.joinToString("\n    ") { it.exposedDao() }}

    fun toBo() = $boName(
        id = id.entityId(),
        ${generators.joinToString(",\n        ") { it.exposedDaoToBo() }}
    )
    
    fun fromBo(bo : $boName) : $exposedDaoName {
        ${generators.joinToString("\n        ") { it.exposedDaoFromBo() }}
        
        return this
    }
}

// -----------------------------------------------------------------------------
//  Exposed Table
// -----------------------------------------------------------------------------
    
object $exposedTableName : LongIdTable("$exposedTableName") {

    ${generators.joinToString("\n    ") { it.exposedTable() }}

    fun toBo(row: ResultRow) = $boName(
        id = row[id].entityId(),
        ${generators.joinToString(",\n        ") { it.exposedTableToBo() }}
    )
}

""".trimIndent()

    // @formatter:on

}