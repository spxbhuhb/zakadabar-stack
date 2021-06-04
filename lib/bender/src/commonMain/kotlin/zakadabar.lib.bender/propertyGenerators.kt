/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

import zakadabar.stack.data.schema.descriptor.*
import zakadabar.stack.text.camelToSnakeCase

abstract class PropertyGenerator(
    open val boDescriptor: BoDescriptor,
    open val property: BoProperty,
    open val typeName: String
) {
    open fun commonImport(): List<String> = emptyList()

    open fun commonDeclaration(): String? =
        "var ${property.name} : ${typeName}${optional}"

    open fun commonSchema() =
        "+ ::${property.name} ${property.constraints.toCode()}"

    open fun browserImport(): List<String> = emptyList()

    open fun browserForm() =
        "+ bo::${property.name}"

    open fun browserTable() =
        "+ ${boDescriptor.className}::${property.name}"

    open fun exposedPaImport(): List<String> = emptyList()

    abstract fun exposedTable(): String?

    open fun exposedTableToBo() =
        "${property.name} = this[table.${property.name}]"

    open fun exposedTableFromBo(): String? =
        "this[table.${property.name}] = bo.${property.name}"

    val optional
        get() = if (property.optional) "?" else ""

    val exposedTableOptional
        get() = if (property.optional) ".nullable()" else ""

    fun String.withoutBo() = if (this.lowercase().endsWith("bo")) this.substring(0, this.length - 2) else this
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
            "+ opt(bo::${property.name}, stringStore.trueText, stringStore.falseText)"
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
        listOf("import kotlinx.datetime.Instant")

    override fun exposedPaImport() = listOf(
        "import org.jetbrains.exposed.sql.`java-time`.timestamp",
        "import kotlinx.datetime.toJavaInstant",
        "import kotlinx.datetime.toKotlinInstant"
    )

    override fun exposedTable() =
        "val ${property.name} = timestamp(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

    override fun exposedTableToBo() =
        "${property.name} = this[table.${property.name}]$optional.toKotlinInstant()"

    override fun exposedTableFromBo() =
        "this[table.${property.name}] = bo.${property.name}$optional.toJavaInstant()"

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

open class EntityIdPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: EntityIdBoProperty
) : PropertyGenerator(boDescriptor, property, "EntityId") {

    override fun commonDeclaration() =
        if (property.name == "id") {
            "override var ${property.name} : EntityId<${property.kClassName}>$optional"
        } else {
            "var ${property.name} : EntityId<${property.kClassName}>$optional"
        }

    override fun commonSchema() =
        "+ ::${property.name}"

    override fun browserForm() =
        if (property.name == "id") {
            "+ bo::id"
        } else {
            "+ select(bo::${property.name}) { ${property.kClassName}.all().by { it.name } } "
        }

    override fun browserTable() =
        if (property.name == "id") {
            "+ ${boDescriptor.className}::id"
        } else {
            "// ${boDescriptor.className}::${property.name} // record id and opt record id is not supported yet "
        }

    override fun exposedPaImport(): List<String> =
        if (property.optional) {
            listOf("import org.jetbrains.exposed.dao.id.EntityID")
        } else {
            emptyList()
        }

    override fun exposedTable() =
        if (property.name == "id") {
            null
        } else {
            "val ${property.name} = reference(\"${property.name.camelToSnakeCase()}\", ${property.kClassName.withoutBo()}ExposedTableGen)$exposedTableOptional"
        }

    override fun exposedTableToBo() =
        "${property.name} = this[table.${property.name}]$optional.entityId()"

    override fun exposedTableFromBo() =
        if (property.name == "id") {
            null
        } else {
            if (property.optional) {
                "this[table.${property.name}] = bo.${property.name}?.let { EntityID(it.toLong(), ${property.kClassName.withoutBo()}ExposedTableGen) }"
            } else {
                "this[table.${property.name}] = bo.${property.name}.toLong()"
            }
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

    override fun exposedPaImport() =
        if (property.optional) {
            listOf("import zakadabar.stack.util.BCrypt")
        } else {
            listOf(
                "import zakadabar.stack.util.BCrypt",
                "import zakadabar.stack.data.builtin.misc.Secret"
            )
        }

    override fun exposedTable() =
        "val ${property.name} = varchar(\"${property.name.camelToSnakeCase()}\", 200)$exposedTableOptional"

    override fun exposedTableToBo() =
        if (property.optional) {
            "${property.name} = null /* do not send out the secret */"
        } else {
            "${property.name} = Secret(\"\") /* do not send out the secret */"
        }

    override fun exposedTableFromBo() =
        if (property.optional) {
            "this[table.${property.name}] = bo.${property.name}?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }"
        } else {
            "this[table.${property.name}] = BCrypt.hashpw(bo.${property.name}.value, BCrypt.gensalt())"
        }
}

open class StringPropertyGenerator(
    boDescriptor: BoDescriptor,
    override val property: StringBoProperty
) : PropertyGenerator(boDescriptor, property, "String") {

    override fun exposedTable(): String {
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

    override fun exposedPaImport() =
        listOf(
            "import zakadabar.stack.backend.util.toStackUuid",
            "import zakadabar.stack.backend.util.toJavaUuid"
        )

    override fun exposedTable() =
        "val ${property.name} = uuid(\"${property.name.camelToSnakeCase()}\")$exposedTableOptional"

    override fun exposedTableToBo() =
        "${property.name} = this[table.${property.name}]$optional.toStackUuid()"

    override fun exposedTableFromBo() =
        "this[table.${property.name}] = bo.${property.name}$optional.toJavaUuid()"
}