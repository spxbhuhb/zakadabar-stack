package zakadabar.core.comm

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.entityId

open class TestPa : ExposedPaBase<TestBo, TestTable>(
    table = TestTable
) {
    override fun ResultRow.toBo() = TestBo(
        id = this[table.id].entityId(),
        name = this[table.name]
    )

    override fun UpdateBuilder<*>.fromBo(bo: TestBo) {
        this[table.name] = bo.name
    }
}
