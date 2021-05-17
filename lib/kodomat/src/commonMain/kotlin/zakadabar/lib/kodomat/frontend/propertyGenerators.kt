/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.text.camelToSnakeCase

open class PropertyGenerator(
    open val descriptor: DescriptorDto,
    open val property: PropertyDto,
    open val typeName: String
) {
    open fun commonImport(): String? = null

    open fun commonDeclaration() =
        "var ${property.name} : ${typeName}${optional}"

    open fun commonSchema() =
        "+ ::${property.name} ${property.constraints.toCode()}"

    open fun browserImport(): String? = null

    open fun browserForm() =
        "+ dto::${property.name}"

    open fun browserTable() =
        "+ ${descriptor.kClassName}::${property.name}"

    open fun exposedBackendImport(): String? = null

    open fun exposedTable(): String {
        TODO()
    }

    open fun exposedTableToDto() =
        "${property.name} = row[${property.name}]"

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
        "val ${property.name} = bool(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class DoublePropertyGenerator(
    descriptor: DescriptorDto,
    override val property: DoublePropertyDto
) : PropertyGenerator(descriptor, property, "Double") {

    override fun exposedTable() =
        "val ${property.name} = double(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class EnumPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: EnumPropertyDto
) : PropertyGenerator(descriptor, property, property.enumName) {

    override fun exposedTable() =
        "val ${property.name} = enumerationByName(\"${property.name.camelToSnakeCase()}\", 20, ${property.enumName}::class)$exposedTableOptional // TODO check size of ${property.enumName} in the exposed table"

}

open class InstantPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: InstantPropertyDto
) : PropertyGenerator(descriptor, property, "Instant") {

    override fun commonImport() : String {
        return if (property.optional) {
            "import kotlinx.datetime.Instant\nimport zakadabar.stack.data.util.InstantAsStringSerializer"
        } else {
            "import kotlinx.datetime.Instant\nimport zakadabar.stack.data.util.OptInstantAsStringSerializer"
        }
    }

    override fun commonDeclaration(): String {
        return if (property.optional) {
            "@Serializable(OptInstantAsStringSerializer::class)\n    ${super.commonDeclaration()}"
        } else {
            "@Serializable(InstantAsStringSerializer::class)\n    ${super.commonDeclaration()}"
        }
    }

    override fun exposedTable() =
        "val ${property.name} = timestamp(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

    override fun exposedTableToDto() =
        if (property.optional) {
            "${property.name} = row[${property.name}]?.toKotlinInstant()"
        } else {
            "${property.name} = row[${property.name}].toKotlinInstant()"
        }

    override fun exposedDaoToDto() =
        if (property.optional) {
            "${property.name} = ${property.name}?.toKotlinInstant()"
        } else {
            "${property.name} = ${property.name}.toKotlinInstant()"
        }

    override fun exposedDaoFromDto() =
        if (property.optional) {
            "${property.name} = dto.${property.name}?.toJavaInstant()"
        } else {
            "${property.name} = dto.${property.name}.toJavaInstant()"
        }
}

open class IntPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: IntPropertyDto
) : PropertyGenerator(descriptor, property, "Int") {

    override fun exposedTable() =
        "val ${property.name} = integer(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class LongPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: LongPropertyDto
) : PropertyGenerator(descriptor, property, "Long") {

    override fun exposedTable() =
        "val ${property.name} = long(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

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
            "+ select(dto::${property.name}) { ${property.kClassName}.all().by { TODO(\"${property.kClassName}.name\") } } "
        }

    override fun browserTable() =
        "// ${descriptor.kClassName}::${property.name} // not supported yet "

    override fun exposedTable() =
        "val ${property.name} = reference(\"${property.name.camelToSnakeCase()}\", ${property.kClassName.withoutDto()}Table).$exposedTableOptional"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]$optional.recordId()"

    override fun exposedDao() =
        "var ${property.name} by ${property.kClassName.toDaoName()} $exposedDaoReference ${descriptor.kClassName.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}$optional.recordId()"

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
        "val ${property.name} = varchar(\"${property.name.camelToSnakeCase()}\", 200)$exposedTableOptional"

    override fun exposedTableToDto() =
        if (property.optional) {
            "${property.name} = null /* do not send out the secret */"
        } else {
            "${property.name} = Secret(\"\") /* do not send out the secret */"
        }

    override fun exposedDaoToDto() =
        if (property.optional) {
            "${property.name} = null /* do not send out the secret */"
        } else {
            "${property.name} = Secret(\"\") /* do not send out the secret */"
        }
    
    override fun exposedDaoFromDto() =
        if (property.optional) {
            "dto.${property.name}?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }"
        } else {
            "BCrypt.hashpw(dto.${property.name}.value, BCrypt.gensalt())"
        }
}

open class StringPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: StringPropertyDto
) : PropertyGenerator(descriptor, property, "String") {

    override fun exposedTable() : String {
        val max = property.constraints.firstOrNull { it.type == ConstraintType.Max } as? ConstraintIntDto
        return if (max?.value != null) {
            "val ${property.name} = varchar(\"${property.name}\", ${max.value})$exposedTableOptional"
        } else {
            "val ${property.name} = text(\"${property.name}\")$exposedTableOptional"
        }
    }
}

open class UuidPropertyGenerator(
    descriptor: DescriptorDto,
    override val property: UuidPropertyDto
) : PropertyGenerator(descriptor, property, "UUID") {

    override fun commonImport() =
        "import zakadabar.stack.util.UUID"

    override fun browserImport() =
        "import zakadabar.stack.util.UUID"

    override fun exposedTable() =
        "val ${property.name} = uuid(\"${property.name}\")$exposedTableOptional"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]$optional.toStackUuid()"
    
    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}$optional.toStackUuid()"

    override fun exposedDaoFromDto() =
        "${property.name} = ${property.name}$optional.toJavaUuid()"
}