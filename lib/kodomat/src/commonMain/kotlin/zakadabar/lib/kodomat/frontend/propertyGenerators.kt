/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*

interface PropertyGenerator {
    fun commonDeclaration(): String
    fun commonSchema(): String
    fun browserForm(): String
    fun browserTable(): String
    fun exposedTable(): String
    fun exposedTableToDto(): String
    fun exposedDao(): String
    fun exposedDaoToDto(): String
    fun exposedDaoFromDto(): String
}

fun String.withoutDto() = if (this.toLowerCase().endsWith("dto")) this.substring(0, this.length-3) else this
fun String.toTableName() = "${withoutDto()}Table"
fun String.toDaoName() = "${withoutDto()}Dao"

open class RecordIdGenerator(
    open val descriptor: DescriptorDto,
    open val property: RecordIdPropertyDto
) : PropertyGenerator {

    override fun commonDeclaration() =
        "var ${property.name} : RecordId<${property.kClassName}>"

    override fun commonSchema() =
        "+ ::${property.name}"

    override fun browserForm() =
        if (property.name == "id") {
            "+ dto::id"
        } else {
            "+ select(dto::${property.name}) { ${property.kClassName}.all().by { it.name } } "
        }

    override fun browserTable() =
        "// ${descriptor.kClassName}::${property::name} // not supported yet "

    override fun exposedTable() =
        "val ${property.name} = reference(\"${property.name}\", ${property.kClassName})"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}].recordId()"

    override fun exposedDao() =
        "var ${property.name} by ${property.kClassName.toDaoName()} referencedOn ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}.recordId()"

    override fun exposedDaoFromDto() =
        "${property.name} = ${property.kClassName.toDaoName()}[dto.${property.name}]"
}

open class BooleanGenerator(
    open val descriptor: DescriptorDto,
    open val property: BooleanPropertyDto
) : PropertyGenerator {

    override fun commonDeclaration() =
        "var ${property.name} : Boolean"

    override fun commonSchema() =
        "+ ::${property.name}"

    override fun browserForm() =
        "+ dto::${property.name}"

    override fun browserTable() =
        "+ ${descriptor.kClassName}::${property::name}"

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]"

    override fun exposedDao() =
        "var ${property.name} by ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}"

    override fun exposedDaoFromDto() =
        "${property.name} = dto.${property.name}"
}

open class OptBooleanGenerator(
    open val descriptor: DescriptorDto,
    open val property: OptBooleanPropertyDto
) : PropertyGenerator {

    override fun commonDeclaration() =
        "var ${property.name} : Boolean?"

    override fun commonSchema() =
        "+ ::${property.name}"

    override fun browserForm() =
        "+ opt(dto::${property.name}, strings.trueText, strings.falseText)"

    override fun browserTable() =
        "+ ${descriptor.kClassName}::${property::name}"

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\").nullable()"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]"

    override fun exposedDao() =
        "var ${property.name} by ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}"

    override fun exposedDaoFromDto() =
        "${property.name} = dto.${property.name}"
}

open class OptRecordIdGenerator(
    open val descriptor: DescriptorDto,
    open val property: OptRecordIdPropertyDto
) : PropertyGenerator {

    override fun commonDeclaration() =
        "var ${property.name} : RecordId<${property.kClassName}>?"

    override fun commonSchema() =
        "+ ::${property.name}"

    override fun browserForm() =
        "+ select(dto::${property.name}) { ${property.kClassName}.all().by { it.name } } "

    override fun browserTable() =
        "// ${descriptor.kClassName}::${property::name} // not supported yet "

    override fun exposedTable() =
        "val ${property.name} = reference(\"${property.name}\", ${property.kClassName}).nullable()"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]?.recordId()"

    override fun exposedDao() =
        "var ${property.name} by ${property.kClassName.toDaoName()} optionalReferencedOn ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}?.recordId()"

    override fun exposedDaoFromDto() =
        "${property.name} = dto.${property.name}?.let { v -> ${property.kClassName.toDaoName()}[v] }"
}