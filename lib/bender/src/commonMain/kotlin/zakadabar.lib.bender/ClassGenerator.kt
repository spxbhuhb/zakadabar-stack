/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import kotlinx.datetime.Clock
import zakadabar.core.schema.descriptor.BoDescriptor
import zakadabar.core.text.camelToSnakeCase

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
package $packageName

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
${generators.map { it.commonImport() }.flatten().distinct().joinToString("\n")}

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
package ${packageName}.browser

import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized
import zakadabar.core.browser.application.target
import ${packageName}.$boName
${generators.map { it.browserImport() }.flatten().distinct().joinToString("\n")}

class $browserCrudName : ZkCrudTarget<$boName>() {
    init {
        companion = $boName.Companion
        boClass = $boName::class
        editorClass = $browserFormName::class
        tableClass = $browserTableName::class
    }
}

class $browserFormName : ZkForm<$boName>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<$browserFormName>()) {
            + section {
                ${generators.joinToString("\n                ") { it.browserForm() }}
            }
        }
    }
}

class $browserTableName : ZkTable<$boName>() {

    override fun onConfigure() {

        crud = target<$browserCrudName>()

        titleText = localized<$browserTableName>()

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
package $packageName

import zakadabar.core.authorize.BusinessLogicBusinessLogicAuthorizer
import zakadabar.core.authorize.EmptyBusinessLogicAuthorizer
import zakadabar.core.business.EntityBusinessLogicBase
import ${packageName}.$boName

open class $businessLogicName : EntityBusinessLogicBase<${boName}>(
    boClass = ${boName}::class
) {

    override val pa = ${baseName}Pa()

    override val BusinessLogicAuthorizer : BusinessLogicAuthorizer<${boName}> = EmptyAuthorizer()
    
}
""".trimIndent()

    // -------------------------------------------------------------------------
    //  Exposed Persistence API
    // -------------------------------------------------------------------------

fun exposedPaGenerator() = """
package $packageName

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import ${packageName}.$boName
${generators.map { it.exposedPaImport() }.flatten().distinct().joinToString("\n")}

open class ${baseName}Pa(
    table : ${baseName}Table = ${baseName}Table()
) : ExposedPaBase<$boName,${baseName}Table>(
    table = table
) {
    override fun ResultRow.toBo() = $boName(
        ${generators.joinToString(",\n        ") { it.exposedTableToBo() }}
    )  

    override fun UpdateBuilder<*>.fromBo(bo: $boName) {
        ${generators.mapNotNull { it.exposedTableFromBo() }.joinToString("\n        ")}
    }
}

open class ${baseName}Table(
    tableName : String = "${baseName.camelToSnakeCase()}"
) : ExposedPaTable<$boName>(
    tableName = tableName
) {

    ${generators.mapNotNull { it.exposedTable()  }.joinToString("\n    ")}

}
""".trimIndent()

    // @formatter:on

}