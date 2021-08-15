/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.entity.builtin

import kotlinx.datetime.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId
import zakadabar.core.util.toJavaUuid
import zakadabar.core.util.toStackUuid
import zakadabar.core.data.Secret
import zakadabar.core.util.BCrypt

open class ExamplePa : ExposedPaBase<ExampleBo,ExampleTable>(
    table = ExampleTable
) {

    fun count() = table.selectAll().count()

    override fun ResultRow.toBo() = ExampleBo(
        id = this[table.id].entityId(),
        booleanValue = this[table.booleanValue],
        doubleValue = this[table.doubleValue],
        enumSelectValue = this[table.enumSelectValue],
        intValue = this[table.intValue],
        instantValue = this[table.instantValue].toKotlinInstant(),
        localDateValue = this[table.localDateValue].toKotlinLocalDate(),
        localDateTimeValue = this[table.localDateTimeValue].toKotlinLocalDateTime(),
        optBooleanValue = this[table.optBooleanValue],
        optDoubleValue = this[table.optDoubleValue],
        optEnumSelectValue = this[table.optEnumSelectValue],
        optInstantValue = this[table.optInstantValue]?.toKotlinInstant(),
        optLocalDateValue = this[table.optLocalDateValue]?.toKotlinLocalDate(),
        optLocalDateTimeValue = this[table.optLocalDateTimeValue]?.toKotlinLocalDateTime(),
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

    override fun UpdateBuilder<*>.fromBo(bo: ExampleBo) {
        this[table.booleanValue] = bo.booleanValue
        this[table.doubleValue] = bo.doubleValue
        this[table.enumSelectValue] = bo.enumSelectValue
        this[table.intValue] = bo.intValue
        this[table.instantValue] = bo.instantValue.toJavaInstant()
        this[table.localDateValue] = bo.localDateValue.toJavaLocalDate()
        this[table.localDateTimeValue] = bo.localDateTimeValue.toJavaLocalDateTime()
        this[table.optBooleanValue] = bo.optBooleanValue
        this[table.optDoubleValue] = bo.optDoubleValue
        this[table.optEnumSelectValue] = bo.optEnumSelectValue
        this[table.optInstantValue] = bo.optInstantValue?.toJavaInstant()
        this[table.optLocalDateValue] = bo.optLocalDateValue?.toJavaLocalDate()
        this[table.optLocalDateTimeValue] = bo.optLocalDateTimeValue?.toJavaLocalDateTime()
        this[table.optIntValue] = bo.optIntValue
        this[table.optSecretValue] = bo.optSecretValue?.let { s -> BCrypt.hashpw(s.value, BCrypt.gensalt()) }
        this[table.optRecordSelectValue] = bo.optRecordSelectValue?.let { EntityID(it.toLong(), ExampleReferenceTable) }
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

object ExampleTable : ExposedPaTable<ExampleBo>(
    tableName = "builtin"
) {

    val booleanValue = bool("boolean_value")
    val doubleValue = double("double_value")
    val enumSelectValue = enumerationByName("enum_select_value", 20, ExampleEnum::class) // TODO check size of ExampleEnum in the exposed table
    val intValue = integer("int_value")
    val instantValue = timestamp("instant_value")
    val localDateValue = date("local_date_value")
    val localDateTimeValue = datetime("local_date_time_value")
    val optBooleanValue = bool("opt_boolean_value").nullable()
    val optDoubleValue = double("opt_double_value").nullable()
    val optEnumSelectValue = enumerationByName("opt_enum_select_value", 20, ExampleEnum::class).nullable() // TODO check size of ExampleEnum in the exposed table
    val optInstantValue = timestamp("opt_instant_value").nullable()
    val optLocalDateValue = date("opt_local_date_value").nullable()
    val optLocalDateTimeValue = datetime("opt_local_date_time_value").nullable()
    val optIntValue = integer("opt_int_value").nullable()
    val optSecretValue = varchar("opt_secret_value", 200).nullable()
    val optRecordSelectValue = reference("opt_record_select_value", ExampleReferenceTable).nullable()
    val optStringValue = varchar("opt_string_value", 100).nullable()
    val optStringSelectValue = varchar("opt_string_select_value", 100).nullable()
    val optTextAreaValue = varchar("opt_text_area_value", 100000).nullable()
    val optUuidValue = uuid("opt_uuid_value").nullable()
    val secretValue = varchar("secret_value", 200)
    val recordSelectValue = reference("record_select_value", ExampleReferenceTable)
    val stringValue = varchar("string_value", 50)
    val stringSelectValue = varchar("string_select_value", 50)
    val textAreaValue = varchar("text_area_value", 2000)
    val uuidValue = uuid("uuid_value")

}