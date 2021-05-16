/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.DescriptorDto


fun dtoGenerator(descriptor: DescriptorDto, generators: List<PropertyGenerator>) =
    """
package ${descriptor.packageName}

import kotlinx.serialization.Serializable
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordDtoCompanion
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.data.schema.DtoSchema

@Serializable
data class ${descriptor.kClassName}(

    override var id: RecordId<${descriptor.kClassName}>,
    ${generators.map { it.commonDeclaration() }.joinToString(",\n    ")}

) : RecordDto<SimpleExampleDto> {

    companion object : RecordDtoCompanion<${descriptor.kClassName}>("${descriptor.kClassName}")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        ${generators.map { it.commonSchema() }.joinToString("\n        ")}
    }

}
""".trimIndent()