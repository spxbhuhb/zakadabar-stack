/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.text.camelToSnakeCase

open class PropertyGenerator(
    open val boDescriptor: BoDescriptor,
    open val property: BoProperty,
    open val typeName: String
) {
    open fun commonImport(): List<String> = emptyList()

    open fun commonDeclaration() =
        "var ${property.name} : ${typeName}${optional}"

    open fun commonSchema() =
        "+ ::${property.name} ${property.constraints.toCode()}"

    open fun browserImport(): List<String> = emptyList()

    open fun browserForm() =
        "+ dto::${property.name}"

    open fun browserTable() =
        "+ ${boDescriptor.className}::${property.name}"

    open fun exposedBackendImport(): List<String> = emptyList()

    open fun exposedTable(): String {
        TODO()
    }

    open fun exposedTableToDto() =
        "${property.name} = row[${property.name}]"

    open fun exposedDao() =
        "var ${property.name} by ${boDescriptor.className.toTableName()}.${property.name}"

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
    boDescriptor: BoDescriptor,
    override val property: BooleanBoProperty
) : PropertyGenerator(boDescriptor, property, "Boolean") {

    override fun browserImport() =
        if (property.optional) {
            listOf("import zakadabar.stack.frontend.application.stringStore")
        } else {
            emptyList()
        }

    override fun browserForm() =
        if (property.optional) {
            "+ opt(dto::${property.name}, stringStore.trueText, stringStore.falseText)"
        } else {
            super.browserForm()
        }

    override fun browserTable() =
        if (property.optional) {
            "// ${super.browserTable()} // opt boolean is not supported yet"
        } else {
            super.browserTable()
        }

    override fun exposedTable() =
        "val ${property.name} = bool(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class DoublePropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: DoubleBoProperty
) : PropertyGenerator(boDescriptor, property, "Double") {

    override fun exposedTable() =
        "val ${property.name} = double(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class EnumPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: EnumBoProperty
) : PropertyGenerator(boDescriptor, property, property.enumName) {

    override fun browserTable() =
        if (property.optional) {
            "// ${super.browserTable()} // opt enum is not supported yet"
        } else {
            super.browserTable()
        }

    override fun exposedTable() =
        "val ${property.name} = enumerationByName(\"${property.name.camelToSnakeCase()}\", 20, ${property.enumName}::class)$exposedTableOptional // TODO check size of ${property.enumName} in the exposed table"

}

open class InstantPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: InstantBoProperty
) : PropertyGenerator(boDescriptor, property, "Instant") {

    override fun commonImport() =
         if (property.optional) {
            listOf("import kotlinx.datetime.Instant", "import zakadabar.stack.data.util.InstantAsStringSerializer")
        } else {
            listOf("import kotlinx.datetime.Instant", "import zakadabar.stack.data.util.OptInstantAsStringSerializer")
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
    boDescriptor: BoDescriptor,
    override val property: IntBoProperty
) : PropertyGenerator(boDescriptor, property, "Int") {

    override fun exposedTable() =
        "val ${property.name} = integer(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class LongPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: LongBoProperty
) : PropertyGenerator(boDescriptor, property, "Long") {

    override fun exposedTable() =
        "val ${property.name} = long(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

}

open class RecordIdPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: RecordIdBoProperty
) : PropertyGenerator(boDescriptor, property, "RecordId") {

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
        "// ${boDescriptor.className}::${property.name} // record id and opt record id is not supported yet "

    override fun exposedTable() =
        "val ${property.name} = reference(\"${property.name.camelToSnakeCase()}\", ${property.kClassName.withoutDto()}Table)$exposedTableOptional"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]$optional.recordId()"

    override fun exposedDao() =
        "var ${property.name} by ${property.kClassName.toDaoName()} $exposedDaoReference ${boDescriptor.className.toTableName()}.${property.name}"

    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}$optional.id$optional.recordId()"

    override fun exposedDaoFromDto() =
        if (property.optional) {
            "${property.name} = dto.${property.name}?.let { ${property.kClassName.toDaoName()}[it] }"
        } else {
            "${property.name} = ${property.kClassName.toDaoName()}[dto.${property.name}]"
        }
}

open class SecretPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: SecretBoProperty
) : PropertyGenerator(boDescriptor, property, "Secret") {

    override fun commonImport() =
        listOf("import zakadabar.stack.data.builtin.misc.Secret")

    override fun browserTable() =
        "// ${super.browserTable()} // not supported yet"

    override fun exposedBackendImport() =
        listOf("import zakadabar.stack.data.builtin.misc.Secret")

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
    boDescriptor: BoDescriptor,
    override val property: StringBoProperty
) : PropertyGenerator(boDescriptor, property, "String") {

    override fun exposedTable() : String {
        val max = property.constraints.firstOrNull { it.type == BoConstraintType.Max } as? IntBoConstraint
        return if (max?.value != null) {
            "val ${property.name} = varchar(\"${property.name.camelToSnakeCase()}\", ${max.value})$exposedTableOptional"
        } else {
            "val ${property.name} = text(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"
        }
    }
}

open class UuidPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: UuidBoProperty
) : PropertyGenerator(boDescriptor, property, "UUID") {

    override fun commonImport() =
        listOf("import zakadabar.stack.util.UUID")

    override fun exposedTable() =
        "val ${property.name} = uuid(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

    override fun exposedTableToDto() =
        "${property.name} = row[${property.name}]$optional.toStackUuid()"
    
    override fun exposedDaoToDto() =
        "${property.name} = ${property.name}$optional.toStackUuid()"

    override fun exposedDaoFromDto() =
        "${property.name} = dto.${property.name}$optional.toJavaUuid()"
}