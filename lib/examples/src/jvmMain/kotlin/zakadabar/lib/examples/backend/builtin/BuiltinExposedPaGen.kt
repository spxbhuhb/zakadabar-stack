/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.builtin

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.data.builtin.ExampleEnum
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId
import zakadabar.stack.backend.util.toJavaUuid
import zakadabar.stack.backend.util.toStackUuid
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.util.BCrypt

/**
 * Exposed based Persistence API for BuiltinBo.
 * 
 * Generated with Bender at 2021-05-30T14:29:11.471Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 * 
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class BuiltinExposedPaGen : ExposedPaBase<BuiltinBo,BuiltinExposedTableGen>(
    table = BuiltinExposedTableGen
) {
    override fun ResultRow.toBo() = BuiltinBo(
        id = this[table.id].entityId(),
        booleanValue = this[table.booleanValue],
        doubleValue = this[table.doubleValue],
        enumSelectValue = this[table.enumSelectValue],
        intValue = this[table.intValue],
        instantValue = this[table.instantValue].toKotlinInstant(),
        optBooleanValue = this[table.optBooleanValue],
        optDoubleValue = this[table.optDoubleValue],
        optEnumSelectValue = this[table.optEnumSelectValue],
        optInstantValue = this[table.optInstantValue]?.toKotlinInstant(),
        optIntValue = this[table.optIntValue],
        optSecretValue = null /* do not send out the secret */,
        optRecordSelectValue = this[table.optRecordSelectValue]?.entityId(),
        optStringValue = this[table.optStringValue],
        optStringSelectValue = this[table.optStringSelectValue],
        optTextAreaValue = this[table.optTextAreaValue],
        optUuidValue = this[table.optUuidValue]?.toStackUuid(),
        secretValue = Secret("") /* do not send out the secret */,
        recordSelectValue = this[table.recordSelectValue].entityId(),
        stringValue = this[table.stringValue],
        stringSelectValue = this[table.stringSelectValue],
        textAreaValue = this[table.textAreaValue],
        uuidValue = this[table.uuidValue].toStackUuid()
    )  

    override fun UpdateBuilder<*>.fromBo(bo: BuiltinBo) {
        this[table.booleanValue] = bo.booleanValue
        this[table.doubleValue] = bo.doubleValue
        this[table.enumSelectValue] = bo.enumSelectValue
        this[table.intValue] = bo.intValue
        this[table.instantValue] = bo.instantValue.toJavaInstant()
        this[table.optBooleanValue] = bo.optBooleanValue
        this[table.optDoubleValue] = bo.optDoubleValue
        this[table.optEnumSelectValue] = bo.optEnumSelectValue
        this[table.optInstantValue] = bo.optInstantValue?.toJavaInstant()
        this[table.optIntValue] = bo.optIntValue
        this[table.optSecretValue] = bo.optSecretValue?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.optRecordSelectValue] = bo.optRecordSelectValue?.let { EntityID(it.toLong(), ExampleReferenceExposedTableGen) }
        this[table.optStringValue] = bo.optStringValue
        this[table.optStringSelectValue] = bo.optStringSelectValue
        this[table.optTextAreaValue] = bo.optTextAreaValue
        this[table.optUuidValue] = bo.optUuidValue?.toJavaUuid()
        this[table.secretValue] = BCrypt.hashpw(bo.secretValue.value, BCrypt.gensalt())
        this[table.recordSelectValue] = bo.recordSelectValue.toLong()
        this[table.stringValue] = bo.stringValue
        this[table.stringSelectValue] = bo.stringSelectValue
        this[table.textAreaValue] = bo.textAreaValue
        this[table.uuidValue] = bo.uuidValue.toJavaUuid()
    }
}

/**
 * Exposed based SQL table for BuiltinBo.
 * 
 * Generated with Bender at 2021-05-30T14:29:11.472Z.
 *
 * **IMPORTANT** Please do not modify this class manually. 
 * 
 * If you need other fields, add them to the business object and then re-generate.
 */
object BuiltinExposedTableGen : ExposedPaTable<BuiltinBo>(
    tableName = "builtin"
) {

    internal val booleanValue = bool("boolean_value")
    internal val doubleValue = double("double_value")
    internal val enumSelectValue = enumerationByName("enum_select_value", 20, ExampleEnum::class) // TODO check size of ExampleEnum in the exposed table
    internal val intValue = integer("int_value")
    internal val instantValue = timestamp("instant_value")
    internal val optBooleanValue = bool("opt_boolean_value").nullable()
    internal val optDoubleValue = double("opt_double_value").nullable()
    internal val optEnumSelectValue = enumerationByName("opt_enum_select_value", 20, ExampleEnum::class).nullable() // TODO check size of ExampleEnum in the exposed table
    internal val optInstantValue = timestamp("opt_instant_value").nullable()
    internal val optIntValue = integer("opt_int_value").nullable()
    internal val optSecretValue = varchar("opt_secret_value", 200).nullable()
    internal val optRecordSelectValue = reference("opt_record_select_value", ExampleReferenceExposedTableGen).nullable()
    internal val optStringValue = varchar("opt_string_value", 100).nullable()
    internal val optStringSelectValue = varchar("opt_string_select_value", 100).nullable()
    internal val optTextAreaValue = varchar("opt_text_area_value", 100000).nullable()
    internal val optUuidValue = uuid("opt_uuid_value").nullable()
    internal val secretValue = varchar("secret_value", 200)
    internal val recordSelectValue = reference("record_select_value", ExampleReferenceExposedTableGen)
    internal val stringValue = varchar("string_value", 50)
    internal val stringSelectValue = varchar("string_select_value", 50)
    internal val textAreaValue = varchar("text_area_value", 2000)
    internal val uuidValue = uuid("uuid_value")

}