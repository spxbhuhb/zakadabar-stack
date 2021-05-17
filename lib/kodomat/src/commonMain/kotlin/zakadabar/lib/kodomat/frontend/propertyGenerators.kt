/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*

open class PropertyGenerator(
    open val descriptor: DescriptorDto,
    open val property: PropertyDto,
    open val typeName: String
) {
    open fun commonImport() : String? = null

    open fun commonDeclaration() =
        "var ${property.name} : ${typeName}${optional}"

    open fun commonSchema() =
        "+ ::${property.name} ${property.constraints.toCode()}"

    open fun browserFormImport() : String? = null

    open fun browserForm() =
        "+ dto::${property.name}"

    open fun browserTableImport() : String? = null

    open fun browserTable() =
        "+ ${descriptor.kClassName}::${property::name}"

    open fun backendImport()  : String? = null

    open fun exposedTableImport()  : String? = null

    open fun exposedTable() : String {
        TODO()
    }

    open fun exposedTableToDto() =
        "${property.name} = row[${property.name}]"

    open fun exposedDaoImport() : String? = null

    open fun exposedDao() =
        "var ${property.name} by ${descriptor.kClassName.toTableName()}.${property.name}"

    open fun exposedDaoToDto() =
        "${property.name} = ${property.name}"

    open fun exposedDaoFromDto() =
        "${property.name} = dto.${property.name}"

    val optional
       get() = if (property.optional) "?" else ""

    val exposedTableOptional
        get() = if (property.optional) ".nullable()" else ""

    val exposedDaoReference
        get() = if (property.optional) "optionalReferencedOn" else "referencedOn"

}

open class BooleanPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: BooleanPropertyDto
) : PropertyGenerator(descriptor, property, "Boolean") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class DoublePropertyGenerator(
    descriptor: DescriptorDto,
    override val property: DoublePropertyDto
) : PropertyGenerator(descriptor, property, "Double") {

    override fun exposedTable() =
        "val ${property.name} = double(\"${property.name}\")$exposedTableOptional"

}

open class EnumPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: EnumPropertyDto
) : PropertyGenerator(descriptor, property, "Enum") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class InstantPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: InstantPropertyDto
) : PropertyGenerator(descriptor, property, "Instant") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class IntPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: IntPropertyDto
) : PropertyGenerator(descriptor, property, "Int") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class LongPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: LongPropertyDto
) : PropertyGenerator(descriptor, property, "Long") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class RecordIdPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: RecordIdPropertyDto
) : PropertyGenerator(descriptor, property, "RecordId") {

    override fun commonDeclaration() =
        "var ${property.name} : RecordId<${property.kClassName}>$optional"

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
        "val ${property.name} = reference(\"${property.name}\", ${property.kClassName}).$exposedTableOptional"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}].recordId()"

    override fun exposedDao() =
        "var ${property.name} by ${property.kClassName.toDaoName()} $exposedDaoReference ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}.recordId()"

    override fun exposedDaoFromDto() =
        if (property.optional) {
            "${property.name} = dto.${property.name}?.let { ${property.kClassName.toDaoName()}[it] }"
        } else {
            "${property.name} = ${property.kClassName.toDaoName()}[dto.${property.name}]"
        }
}

open class SecretPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: SecretPropertyDto
) : PropertyGenerator(descriptor, property, "Secret") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}

open class StringPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: StringPropertyDto
) : PropertyGenerator(descriptor, property, "String") {

    override fun exposedTable() =
        "val ${property.name} = text(\"${property.name}\")$exposedTableOptional"

}

open class UuidPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: UuidPropertyDto
) : PropertyGenerator(descriptor, property, "Uuid") {

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name}\")$exposedTableOptional"

}