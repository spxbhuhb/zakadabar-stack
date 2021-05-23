/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import zakadabar.stack.data.schema.descriptor.BoDescriptor
import zakadabar.stack.text.camelToSnakeCase

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
        get() = if (boName.lowercase().endsWith("bo")) boName.substring(0, boName.length-2) else boName

    open val browserCrudName
        get() = baseName + "Crud"
    open val browserTableName
        get() = baseName + "Table"
    open val browserFormName
        get() = baseName + "Form"

    // @formatter:off

    // -------------------------------------------------------------------------
    //  Common
    // -------------------------------------------------------------------------

fun commonGenerator() = """
package $packageName

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
${generators.map { it.commonImport() }.flatten().distinct().joinToString("\n")}

@Serializable
class ${boName}(

    override var id: EntityId<$boName>,
    ${generators.joinToString(",\n    ") { it.commonDeclaration() }}

) : EntityBo<$boName> {

    companion object : EntityBoCompanion<$boName>("$namespace")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        ${generators.joinToString("\n        ") { it.commonSchema() }}
    }

}
""".trimIndent()

    // -------------------------------------------------------------------------
    //  Frontend
    // -------------------------------------------------------------------------

fun browserFrontendGenerator() = """
package ${packageName}.frontend

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
        boClass = $boName::class
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

fun businessLogicGenerator() = """
package ${packageName}.backend

import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase
import ${packageName}.data.$boName
${generators.map { it.exposedPaImport() }.flatten().distinct().joinToString("\n")}

class ${baseName}Bl : EntityBusinessLogicBase<${boName}>() {

    override val boClass = ${boName}::class

    override val pa = ${baseName}ExposedCrudPa()

    override val authorizer = SimpleRoleAuthorizer<${boName}> {
        list = StackRoles.siteMember
        read = StackRoles.siteMember
        create = StackRoles.securityOfficer
        update = StackRoles.securityOfficer
        delete = StackRoles.securityOfficer
    }
    
}
""".trimIndent()

fun exposedPaGenerator() = """
package ${packageName}.backend

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.stack.backend.data.entity.EntityPersistenceApiBase
import zakadabar.stack.backend.data.entity.ExposedPersistenceApi
import zakadabar.stack.backend.data.entityId
import zakadabar.stack.backend.data.get
import zakadabar.stack.data.entity.EntityId
import ${packageName}.data.$boName
${generators.map { it.exposedPaImport() }.flatten().distinct().joinToString("\n")}

class ${baseName}ExposedCrudPa : EntityPersistenceApiBase<${boName}>(), ExposedPersistenceApi {

    override fun onModuleLoad() {
        super.onModuleLoad()
        + ${baseName}ExposedTable
    }

    override fun list(): List<${boName}> {
        return ${baseName}ExposedTable
            .selectAll()
            .map { ${baseName}ExposedTable.toBo(it) }
    }

    override fun create(bo: ${boName}): ${boName} {
        val id = ${baseName}ExposedTable
            .insertAndGetId {
                fromBo(it, bo)
            }
        bo.id = EntityId(id.value)
        return bo
    }

    override fun read(entityId: EntityId<${boName}>) : $boName {
        return ${baseName}ExposedTable
            .select { ${baseName}ExposedTable.id eq entityId.toLong() }
            .first()
            .let { ${baseName}ExposedTable.toBo(it) }
    }

    override fun update(bo: ${boName}): $boName {
        ${baseName}ExposedTable
            .update({ ${baseName}ExposedTable.id eq bo.id.toLong() })
            { fromBo(it, bo) }
        return bo
    }

    override fun delete(entityId: EntityId<${boName}>) {
        ${baseName}ExposedTable
            .deleteWhere { ${baseName}ExposedTable.id eq entityId.toLong() }
    }

}

object ${baseName}ExposedTable : LongIdTable("${boName.camelToSnakeCase()}") {

    ${generators.joinToString("\n    ") { it.exposedTable() }}

    fun toBo(row: ResultRow) = $boName(
        id = row[id].entityId(),
        ${generators.joinToString(",\n        ") { it.exposedTableToBo() }}
    )

    fun fromBo(statement: UpdateBuilder<*>, bo: $boName) {
        ${generators.joinToString("\n        ") { it.exposedTableFromBo() }}
    }

}


""".trimIndent()

    // @formatter:on

}