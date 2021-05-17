/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.DescriptorDto

open class ClassGenerator(
    protected val descriptor : DescriptorDto,
    open val generators: List<PropertyGenerator>
) {

    val packageName = descriptor.packageName
    val className = descriptor.kClassName
    val namespace = descriptor.dtoNamespace
    val tableName = descriptor.kClassName.toTableName()
    val daoName = descriptor.kClassName.toDaoName()

    fun dtoGenerator() =
        """
package ${descriptor.packageName}

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema
${generators.mapNotNull { it.commonImport() } .joinToString("\n")}

@Serializable
class ${packageName}(

    override var id: RecordId<$className>,
    ${generators.joinToString(",\n    ") { it.commonDeclaration() }}

) : RecordDto<SimpleExampleDto> {

    companion object : RecordDtoCompanion<$className>("$namespace")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        ${generators.joinToString("\n        ") { it.commonSchema() }}
    }

}
""".trimIndent()

    fun tableGenerator(descriptor: DescriptorDto, generators: List<PropertyGenerator>) =
        """
package ${packageName}

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
${generators.mapNotNull { it.backendImport() } .joinToString("\n")}

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