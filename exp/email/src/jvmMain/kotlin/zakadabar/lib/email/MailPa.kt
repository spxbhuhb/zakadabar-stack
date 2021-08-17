/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import zakadabar.core.persistence.exposed.ExposedPaBase
import zakadabar.core.persistence.exposed.entityId
import zakadabar.lib.accounts.persistence.AccountPrivateExposedTableCommon

open class MailPa(
    table: MailTable = MailTable()
) : ExposedPaBase<Mail, MailTable>(
    table = table
) {
    override fun ResultRow.toBo() = Mail(
        id = this[table.id].entityId(),
        status = this[table.status],
        createdBy = this[table.createdBy]?.entityId(),
        createdAt = this[table.createdAt]?.toKotlinInstant(),
        sentAt = this[table.sentAt]?.toKotlinInstant(),
        sensitive = this[table.sensitive],
        recipients = this[table.recipients],
        subject = this[table.subject]
    )

    override fun UpdateBuilder<*>.fromBo(bo: Mail) {
        this[table.status] = bo.status
        this[table.createdBy] = bo.createdBy?.let { EntityID(it.toLong(), AccountPrivateExposedTableCommon) }
        this[table.createdAt] = bo.createdAt?.toJavaInstant()
        this[table.sentAt] = bo.sentAt?.toJavaInstant()
        this[table.recipients] = bo.recipients
        this[table.sensitive] = bo.sensitive
        this[table.subject] = bo.subject
    }
}