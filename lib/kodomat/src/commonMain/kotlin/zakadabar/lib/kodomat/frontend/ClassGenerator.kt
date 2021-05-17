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
    open val className
        get() = descriptor.kClassName
    open val namespace
        get() = descriptor.dtoNamespace
    open val tableName
        get() = descriptor.kClassName.toTableName()
    open val daoName
        get() = descriptor.kClassName.toDaoName()

    fun dtoGenerator() =
        """
package ${descriptor.packageName}

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
${generators.mapNotNull { it.commonImport() }.joinToString("\n")}

@Serializable
class ${className}(

    override var id: RecordId<$className>,
    ${generators.joinToString(",\n    ") { it.commonDeclaration() }}

) : RecordDto<$className> {

    companion object : RecordDtoCompanion<$className>("$namespace")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        ${generators.joinToString("\n        ") { it.commonSchema() }}
    }

}
""".trimIndent()

    fun backendGenerator() =
        """
package $packageName

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor
import ${packageName}.$className
${generators.mapNotNull { it.backendImport() }.joinToString("\n")}

object ${className.withoutDto()}Backend : RecordBackend<$className>() {

    override val dtoClass = $className::class

    override fun onModuleLoad() {
        + $tableName
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        $tableName
            .selectAll()
            .map($tableName::toDto)
            
    }

    override fun create(executor: Executor, dto: $className) = transaction {

        authorize(executor, StackRoles.siteMember)

        $daoName
            .new { fromDto(dto) }
            .toDto()
           
    }

    override fun read(executor: Executor, recordId: RecordId<$className>) = transaction {

        authorize(true)

        $daoName[recordId].toDto()
        
    }

    override fun update(executor: Executor, dto: $className) = transaction {

        authorize(executor, StackRoles.siteMember)

        $daoName[dto.id]
            .fromDto(dto)
            .toDto()
            
    }

    override fun delete(executor: Executor, recordId: RecordId<$className>) = transaction {

        authorize(executor, StackRoles.siteMember)

        $daoName[recordId].delete()
     
    }

}
""".trimIndent()

}