/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import kotlinx.datetime.Clock
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
        get() = if (boName.lowercase().endsWith("bo")) boName.substring(0, boName.length - 2) else boName

    open val browserCrudName
        get() = baseName + "Crud"
    open val browserTableName
        get() = baseName + "Table"
    open val browserFormName
        get() = baseName + "Form"

    open val businessLogicName
        get() = baseName + "Bl"

    // @formatter:off

    // -------------------------------------------------------------------------
    //  Common
    // -------------------------------------------------------------------------

fun commonGenerator() = """
package $packageName.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
${generators.map { it.commonImport() }.flatten().distinct().joinToString("\n")}

/**
 * Business Object of $boName.
 * 
 * Generated with Bender at ${Clock.System.now()}.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class ${boName}(

    ${generators.mapNotNull { it.commonDeclaration() }.joinToString(",\n    ")}

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
package ${packageName}.frontend.pages

import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.application.target
import ${packageName}.data.$boName

${generators.map { it.browserImport() }.flatten().distinct().joinToString("\n")}

/**
 * CRUD target for [$boName].
 * 
 * Generated with Bender at ${Clock.System.now()}.
 */
class $browserCrudName : ZkCrudTarget<$boName>() {
    init {
        companion = $boName.Companion
        boClass = $boName::class
        pageClass = $browserFormName::class
        tableClass = $browserTableName::class
    }
}

/**
 * Form for [$boName].
 * 
 * Generated with Bender at ${Clock.System.now()}.
 */
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

/**
 * Table for [$boName].
 * 
 * Generated with Bender at ${Clock.System.now()}.
 */
class $browserTableName : ZkTable<$boName>() {

    override fun onConfigure() {

        crud = target<$browserCrudName>()

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
    //  Business Logic
    // -------------------------------------------------------------------------

fun businessLogicGenerator() = """
package ${packageName}.backend

import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import ${packageName}.data.$boName

/**
 * Business Logic for ${boName}.
 * 
 * Generated with Bender at ${Clock.System.now()}.
 */
open class $businessLogicName : EntityBusinessLogicBase<${boName}>(
    boClass = ${boName}::class
) {

    override val pa = ${baseName}ExposedPaGen()

    override val authorizer : Authorizer<${boName}> = EmptyAuthorizer()
    
}
""".trimIndent()

    // -------------------------------------------------------------------------
    //  Exposed Persistence API
    // -------------------------------------------------------------------------

fun exposedPaGenerator() = """
package ${packageName}.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import ${packageName}.data.$boName
${generators.map { it.exposedPaImport() }.flatten().distinct().joinToString("\n")}

/**
 * Exposed based Persistence API for ${boName}.
 * 
 * Generated with Bender at ${Clock.System.now()}.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class ${baseName}ExposedPaGen : ExposedPaBase<$boName,${baseName}ExposedTableGen>(
    table = ${baseName}ExposedTableGen
) {
    override fun ResultRow.toBo() = $boName(
        ${generators.joinToString(",\n        ") { it.exposedTableToBo() }}
    )  

    override fun UpdateBuilder<*>.fromBo(bo: $boName) {
        ${generators.mapNotNull { it.exposedTableFromBo() }.joinToString("\n        ")}
    }
}

/**
 * Exposed based SQL table for ${boName}.
 * 
 * Generated with Bender at ${Clock.System.now()}.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object ${baseName}ExposedTableGen : ExposedPaTable<$boName>(
    tableName = "${baseName.camelToSnakeCase()}"
) {

    ${generators.mapNotNull { it.exposedTable()?.let { dl -> "internal $dl" }  }.joinToString("\n    ")}

}
""".trimIndent()

    // @formatter:on

}