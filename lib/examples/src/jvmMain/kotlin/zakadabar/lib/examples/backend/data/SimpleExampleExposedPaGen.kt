package zakadabar.lib.examples.backend.data

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.stack.backend.exposed.ExposedPaBase
import zakadabar.stack.backend.exposed.ExposedPaTable
import zakadabar.stack.backend.exposed.entityId


/**
 * Exposed based Persistence API for SimpleExampleBo.
 *
 * Generated with Bender at 2021-05-25T19:43:21.158Z.
 *
 * **IMPORTANT** Please do not modify this class manually, see extending patterns below.
 *
 * - If you need other fields, add them to the business object and then re-generate.
 * - If you need other functions, please extend with `Gen` removed from the name.
 */
open class SimpleExampleExposedPaGen : ExposedPaBase<SimpleExampleBo, SimpleExampleExposedTableGen>(
    table = SimpleExampleExposedTableGen
) {
    override fun ResultRow.toBo() = SimpleExampleBo(
        id = this[table.id].entityId(),
        name = this[SimpleExampleExposedTableGen.name]
    )

    override fun UpdateBuilder<*>.fromBo(bo: SimpleExampleBo) {
        this[SimpleExampleExposedTableGen.name] = bo.name
    }
}

/**
 * Exposed based SQL table for SimpleExampleBo.
 *
 * Generated with Bender at 2021-05-25T19:43:21.158Z.
 *
 * **IMPORTANT** Please do not modify this class manually.
 *
 * If you need other fields, add them to the business object and then re-generate.
 */
object SimpleExampleExposedTableGen : ExposedPaTable<SimpleExampleBo>(
    tableName = "simple_example"
) {

    internal val name = varchar("name", 30)

}