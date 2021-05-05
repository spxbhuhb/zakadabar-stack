/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.util.BCrypt

class BuiltinDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<BuiltinDao>(BuiltinTable)

    var booleanValue by BuiltinTable.booleanValue
    var doubleValue by BuiltinTable.doubleValue
    var enumSelectValue by BuiltinTable.enumSelectValue
    var intValue by BuiltinTable.intValue
    var instantValue by BuiltinTable.instantValue
    var optBooleanValue by BuiltinTable.optBooleanValue
    var optDoubleValue by BuiltinTable.optDoubleValue
    var optEnumSelectValue by BuiltinTable.optEnumSelectValue
    var optInstantValue by BuiltinTable.optInstantValue
    var optIntValue by BuiltinTable.optIntValue
    var optSecretValue by BuiltinTable.optSecretValue
    var optRecordSelectValue by ExampleReferenceDao optionalReferencedOn BuiltinTable.optRecordSelectValue
    var optStringValue by BuiltinTable.optStringValue
    var optStringSelectValue by BuiltinTable.optStringSelectValue
    var optTextAreaValue by BuiltinTable.optTextAreaValue
    var optUuidValue by BuiltinTable.optUuidValue
    var secretValue by BuiltinTable.secretValue
    var recordSelectValue by ExampleReferenceDao referencedOn BuiltinTable.recordSelectValue
    var stringValue by BuiltinTable.stringValue
    var stringSelectValue by BuiltinTable.stringSelectValue
    var textAreaValue by BuiltinTable.textAreaValue
    var uuidValue by BuiltinTable.uuidValue

    fun toDto() = zakadabar.lib.examples.data.builtin.BuiltinDto(
        id = id.recordId(),
        booleanValue = booleanValue,
        doubleValue = doubleValue,
        enumSelectValue = enumSelectValue,
        intValue = intValue,
        instantValue = instantValue.toKotlinInstant(),
        optBooleanValue = optBooleanValue,
        optDoubleValue = optDoubleValue,
        optEnumSelectValue = optEnumSelectValue,
        optInstantValue = optInstantValue?.toKotlinInstant(),
        optIntValue = optIntValue,
        optSecretValue = null, // never send out secrets
        optRecordSelectValue = optRecordSelectValue?.id?.recordId(),
        optStringValue = optStringSelectValue,
        optStringSelectValue = optStringSelectValue,
        optTextAreaValue = optTextAreaValue,
        optUuidValue = optUuidValue?.toStackUuid(),
        secretValue = Secret(""), // never send out secrets
        recordSelectValue = recordSelectValue.id.recordId(),
        stringValue = stringValue,
        stringSelectValue = stringSelectValue,
        textAreaValue = textAreaValue,
        uuidValue = uuidValue.toStackUuid()
    )

    fun fromDto(dto: zakadabar.lib.examples.data.builtin.BuiltinDto): BuiltinDao {
        booleanValue = dto.booleanValue
        doubleValue = dto.doubleValue
        enumSelectValue = dto.enumSelectValue
        intValue = dto.intValue
        instantValue = dto.instantValue.toJavaInstant()
        optBooleanValue = dto.optBooleanValue
        optDoubleValue = dto.optDoubleValue
        optEnumSelectValue = dto.optEnumSelectValue
        optInstantValue = dto.optInstantValue?.toJavaInstant() ?: (if (dto.booleanValue) Clock.System.now().toJavaInstant() else null)
        optIntValue = dto.optIntValue
        optSecretValue = dto.optSecretValue?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        optRecordSelectValue = dto.optRecordSelectValue?.let { v -> ExampleReferenceDao[v] }
        optStringValue = dto.optStringSelectValue
        optStringSelectValue = dto.optStringSelectValue
        optTextAreaValue = dto.optTextAreaValue
        optUuidValue = dto.optUuidValue?.toJavaUuid()
        secretValue = BCrypt.hashpw(dto.secretValue.value, BCrypt.gensalt())
        recordSelectValue = ExampleReferenceDao[dto.recordSelectValue]
        stringValue = dto.stringValue
        stringSelectValue = dto.stringSelectValue
        textAreaValue = dto.textAreaValue
        uuidValue = dto.uuidValue.toJavaUuid()

        return this
    }

}