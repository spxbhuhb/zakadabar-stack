/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.DescriptorDto

open class ClassGenerator {

    lateinit var descriptor: DescriptorDto
    lateinit var generators: List<PropertyGenerator>

    open val packageName
        get() = descriptor.packageName
    open val dtoName
        get() = descriptor.kClassName
    open val namespace
        get() = descriptor.dtoNamespace
    open val baseName
        get() = dtoName.withoutDto()

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
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
${generators.map { it.commonImport() }.flatten().distinct().joinToString("\n")}

@Serializable
class ${dtoName}(

    override var id: RecordId<$dtoName>,
    ${generators.joinToString(",\n    ") { it.commonDeclaration() }}

) : RecordDto<$dtoName> {

    companion object : RecordDtoCompanion<$dtoName>("$namespace")

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

import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.application.translate

import ${packageName}.data.$dtoName

${generators.map { it.browserImport() }.flatten().distinct().joinToString("\n")}

// -----------------------------------------------------------------------------
//  Crud
// -----------------------------------------------------------------------------

object $browserCrudName : ZkCrudTarget<$dtoName>() {
    init {
        companion = $dtoName.Companion
        dtoClass = $dtoName::class
        pageClass = $browserFormName::class
        tableClass = $browserTableName::class
    }
}

// -----------------------------------------------------------------------------
//  Form
// -----------------------------------------------------------------------------

class $browserFormName : ZkForm<$dtoName>() {
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

class $browserTableName : ZkTable<$dtoName>() {

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
package ${packageName}.backend.record

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
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor
import ${packageName}.data.$dtoName
${generators.map { it.exposedBackendImport() }.flatten().distinct().joinToString("\n")}

// -----------------------------------------------------------------------------
//  Record Backend
// -----------------------------------------------------------------------------

object ${dtoName.withoutDto()}Backend : RecordBackend<$dtoName>() {

    override val dtoClass = $dtoName::class

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

    override fun create(executor: Executor, dto: $dtoName) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName
            .new { fromDto(dto) }
            .toDto()
           
    }

    override fun read(executor: Executor, recordId: RecordId<$dtoName>) = transaction {

        authorize(true)

        $exposedDaoName[recordId].toDto()
        
    }

    override fun update(executor: Executor, dto: $dtoName) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName[dto.id]
            .fromDto(dto)
            .toDto()
            
    }

    override fun delete(executor: Executor, recordId: RecordId<$dtoName>) = transaction {

        authorize(executor, StackRoles.siteMember)

        $exposedDaoName[recordId].delete()
     
    }

}

// -----------------------------------------------------------------------------
//  Exposed DAO
// -----------------------------------------------------------------------------

class $exposedDaoName(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<$exposedDaoName>($exposedTableName)

    ${generators.joinToString("\n    ") { it.exposedDao() }}

    fun toDto() = $dtoName(
        id = id.recordId(),
        ${generators.joinToString(",\n        ") { it.exposedDaoToDto() }}
    )
    
    fun fromDto(dto : $dtoName) : $exposedDaoName {
        ${generators.joinToString("\n        ") { it.exposedDaoFromDto() }}
        
        return this
    }
}

// -----------------------------------------------------------------------------
//  Exposed Table
// -----------------------------------------------------------------------------
    
object $exposedTableName : LongIdTable("$exposedTableName") {

    ${generators.joinToString("\n    ") { it.exposedTable() }}

    fun toDto(row: ResultRow) = $dtoName(
        id = row[id].recordId(),
        ${generators.joinToString(",\n        ") { it.exposedTableToDto() }}
    )
}

""".trimIndent()

    // @formatter:on

}