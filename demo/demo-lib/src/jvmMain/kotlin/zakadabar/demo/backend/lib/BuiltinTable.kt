/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.backend.lib

import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.demo.data.builtin.ExampleEnum
import zakadabar.stack.backend.util.toStackUuid
import zakadabar.stack.data.builtin.misc.Secret

object BuiltinTable : LongIdTable("builtin") {

    val booleanValue = bool("booleanValue")
    val doubleValue = double("doubleValue")
    val enumSelectValue = enumerationByName("enumSelectValue", 20, ExampleEnum::class)
    val intValue = integer("intValue")
    val instantValue = timestamp("instantValue")
    val optBooleanValue = bool("optBooleanValue").nullable()
    val optDoubleValue = double("optDoubleValue").nullable()
    val optEnumSelectValue = enumerationByName("optEnumSelectValue", 20, ExampleEnum::class).nullable()
    val optInstantValue = timestamp("optInstantValue").nullable()
    val optIntValue = integer("optIntValue").nullable()
    val optSecretValue = varchar("optSecretValue", 50).nullable()
    val optRecordSelectValue = reference("optRecordSelectValue", ExampleReferenceTable).nullable() // the encoded secret is longer than the plain text value
    val optStringValue = varchar("optStringValue", 100).nullable()
    val optStringSelectValue = varchar("optStringSelectValue", 100).nullable()
    val optTextAreaValue = text("optTextAreaValue").nullable()
    val optUuidValue = uuid("optUuidValue").nullable()
    val secretValue = varchar("secretValue", 200) // the encoded secret is longer than the plain text value
    val recordSelectValue = reference("recordSelectValue", ExampleReferenceTable)
    val stringValue = varchar("stringValue", 50)
    val stringSelectValue = varchar("stringSelectValue", 50)
    val textAreaValue = text("textAreaValue")
    val uuidValue = uuid("uuidValue")

    fun toDto(row: ResultRow) = BuiltinDto(
        id = row[id].value,
        booleanValue = row[booleanValue],
        doubleValue = row[doubleValue],
        enumSelectValue = row[enumSelectValue],
        intValue = row[intValue],
        instantValue = row[instantValue].toKotlinInstant(),
        optBooleanValue = row[optBooleanValue],
        optDoubleValue = row[optDoubleValue],
        optEnumSelectValue = row[optEnumSelectValue],
        optInstantValue = row[optInstantValue]?.toKotlinInstant(),
        optIntValue = row[optIntValue],
        optSecretValue = null, // never send out secrets
        optRecordSelectValue = row[recordSelectValue].value,
        optStringValue = row[optStringSelectValue],
        optStringSelectValue = row[optStringSelectValue],
        optTextAreaValue = row[optTextAreaValue],
        optUuidValue = row[optUuidValue]?.toStackUuid(),
        secretValue = Secret(""), // never send out secrets
        recordSelectValue = row[recordSelectValue].value,
        stringValue = row[stringValue],
        stringSelectValue = row[stringSelectValue],
        textAreaValue = row[textAreaValue],
        uuidValue = row[uuidValue].toStackUuid()
    )

}