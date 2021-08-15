/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.email

import org.jetbrains.exposed.sql.`java-time`.timestamp
import zakadabar.lib.accounts.backend.pa.AccountPrivateExposedTableCommon
import zakadabar.core.persistence.exposed.ExposedPaTable

open class MailTable(
    tableName : String = "mail"
) : ExposedPaTable<Mail>(
    tableName = tableName
) {

    val createdBy = reference("created_by", AccountPrivateExposedTableCommon).nullable()
    val createdAt = timestamp("created_at").nullable()
    val sentAt = timestamp("sent_at").nullable()
    val status = enumerationByName("status", 20, MailStatus::class)

    val sensitive = bool("sensitive")
    val recipients = text("recipients")
    val subject = varchar("subject", 200)

}