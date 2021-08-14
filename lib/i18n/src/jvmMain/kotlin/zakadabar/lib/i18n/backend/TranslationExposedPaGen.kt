package zakadabar.lib.i18n.backend

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.i18n.data.TranslationBo
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.ExposedPaTable
import zakadabar.core.persistence.exposed.entityId


/**
 * Exposed based Persistence API for TranslationBo.
 *
 * Generated with Bender at 2021-05-30T09:38:55.863Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class TranslationExposedPaGen : ExposedPaBase<TranslationBo,TranslationExposedTableGen>(
    table = TranslationExposedTableGen
) {
    override fun ResultRow.toBo() = TranslationBo(
        id = this[table.id].entityId(),
        key = this[table.key],
        locale = this[table.locale].entityId(),
        value = this[table.value]
    )

    override fun UpdateBuilder<*>.fromBo(bo: TranslationBo) {
        this[table.key] = bo.key
        this[table.locale] = bo.locale.toLong()
        this[table.value] = bo.value
    }
}

/**
 * Exposed based SQL table for TranslationBo.
 *
 * Generated with Bender at 2021-05-30T09:38:55.863Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object TranslationExposedTableGen : ExposedPaTable<TranslationBo>(
    tableName = "translation"
) {

    internal val key = varchar("key", 100)
    internal val locale = reference("locale", LocaleExposedTableGen)
    internal val value = text("value")

}